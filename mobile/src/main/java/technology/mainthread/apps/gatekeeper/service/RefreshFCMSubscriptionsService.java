package technology.mainthread.apps.gatekeeper.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

import io.reactivex.Observable;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.R;

public class RefreshFCMSubscriptionsService extends IntentService {

    private static final String ACTION_REFRESH_FCM_SUBSCRIPTIONS = "ACTION_REFRESH_FCM_SUBSCRIPTIONS";

    @Inject
    FirebaseMessaging messaging;

    public RefreshFCMSubscriptionsService() {
        super(RefreshFCMSubscriptionsService.class.getSimpleName());
    }

    public static Intent getRefreshFCMSubscriptionsIntent(Context context) {
        Intent intent = new Intent(context, RefreshFCMSubscriptionsService.class);
        intent.setAction(ACTION_REFRESH_FCM_SUBSCRIPTIONS);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GatekeeperApp.get(this).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_REFRESH_FCM_SUBSCRIPTIONS.equals(intent.getAction())) {
            refreshSubscriptions();
        }
    }

    private void refreshSubscriptions() {
        String[] allSubscriptions = getResources().getStringArray(R.array.notif_subscriptions_values);
        Observable.fromArray(allSubscriptions).forEach(subscription -> messaging.subscribeToTopic(subscription));
    }
}
