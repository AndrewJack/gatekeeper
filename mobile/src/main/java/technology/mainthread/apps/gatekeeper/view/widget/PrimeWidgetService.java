package technology.mainthread.apps.gatekeeper.view.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.Subscriptions;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.data.AppStateController;
import technology.mainthread.apps.gatekeeper.data.service.GatekeeperService;
import technology.mainthread.apps.gatekeeper.service.GatekeeperIntentService;
import technology.mainthread.apps.gatekeeper.util.RxSchedulerUtil;

public class PrimeWidgetService extends Service {

    private static final String ACTION_REFRESH = "ACTION_REFRESH";
    private static final String ACTION_BUTTON = "ACTION_BUTTON";
    private static final String EXTRA_WIDGET_IDS = "EXTRA_WIDGET_IDS";

    @Inject
    WidgetViewModel widgetViewModel;
    @Inject
    GatekeeperService gatekeeperService;
    @Inject
    AppStateController appStateController;

    private Subscription subscription = Subscriptions.empty();

    @Override
    public void onCreate() {
        super.onCreate();
        GatekeeperApp.get(this).inject(this);

        subscription = appStateController.getObservable()
                .compose(RxSchedulerUtil.applySchedulers())
                .subscribe(event -> widgetViewModel.updateWidgetState(event.gatekeeperState()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        int[] widgetIds = intent.getIntArrayExtra(EXTRA_WIDGET_IDS);

        if (widgetIds != null) {
            if (ACTION_REFRESH.equals(action)) {
                widgetViewModel.refreshWidget(widgetIds);
            } else if (ACTION_BUTTON.equals(action)) {
//                widgetViewModel.showProgress(widgetIds); TODO: listen for changes
                startService(GatekeeperIntentService.getPrimeGatekeeperIntent(this));
            }
        }

        return START_NOT_STICKY;
    }

    public static Intent getRefreshWidgetsIntent(Context context, int[] widgetIds) {
        Intent intent = new Intent(context, PrimeWidgetService.class);
        intent.setAction(ACTION_REFRESH);
        intent.putExtra(EXTRA_WIDGET_IDS, widgetIds);
        return intent;
    }

    public static Intent getButtonPressedIntent(Context context, int[] widgetIds) {
        Intent intent = new Intent(context, PrimeWidgetService.class);
        intent.setAction(ACTION_BUTTON);
        intent.putExtra(EXTRA_WIDGET_IDS, widgetIds);
        return intent;
    }

}
