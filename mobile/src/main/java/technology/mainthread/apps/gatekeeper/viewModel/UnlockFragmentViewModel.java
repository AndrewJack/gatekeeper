package technology.mainthread.apps.gatekeeper.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import rx.Observable;
import technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper;
import technology.mainthread.apps.gatekeeper.data.AppStateController;
import technology.mainthread.apps.gatekeeper.model.event.AppEvent;
import technology.mainthread.apps.gatekeeper.model.event.AppEventType;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus;

import static technology.mainthread.apps.gatekeeper.service.GatekeeperStateService.ACTION_CHECK_STATE;
import static technology.mainthread.apps.gatekeeper.service.GatekeeperStateService.ACTION_PRIME;
import static technology.mainthread.apps.gatekeeper.service.GatekeeperStateService.ACTION_UNLOCK;
import static technology.mainthread.apps.gatekeeper.service.GatekeeperStateService.getGatekeeperStateIntent;

public class UnlockFragmentViewModel extends BaseObservable {

    private final Context context;
    private final View rootView;
    private final AppStateController appStateController;
    private final Observable.Transformer lifecycleTransformer;

    public ObservableBoolean loading = new ObservableBoolean();

    public ObservableField<String> deviceStatus = new ObservableField<>();

    public UnlockFragmentViewModel(Context context,
                                   View rootView,
                                   AppStateController appStateController,
                                   Observable.Transformer lifecycleTransformer) {
        this.context = context;
        this.rootView = rootView;
        this.appStateController = appStateController;
        this.lifecycleTransformer = lifecycleTransformer;
    }

    /**
     * Setup the device status thing.
     */
    public void initialize() {
        appStateController.getObservable()
                .compose(lifecycleTransformer)
                .compose(RxSchedulerHelper.<DeviceStatus>applySchedulers())
                .subscribe(event -> onAppEvent((AppEvent) event));

        deviceStatus.set(appStateController.getLastKnownGatekeeperState());
        context.startService(getGatekeeperStateIntent(context, ACTION_CHECK_STATE));
    }

    public void onUnlockClicked(View view) {
        context.startService(getGatekeeperStateIntent(context, ACTION_UNLOCK));
    }

    public void onPrimeClicked(View view) {
        context.startService(getGatekeeperStateIntent(context, ACTION_PRIME));
    }

    private void onAppEvent(AppEvent appEvent) {
        deviceStatus.set(appEvent.gatekeeperState());

        switch (appEvent.appState()) {
            case AppEventType.CHECKING:
            case AppEventType.UNLOCKING:
            case AppEventType.PRIMING:
                loading.set(true);
                break;
            case AppEventType.COMPLETE:
                loading.set(false);
                displaySnackbar(appEvent.message());
                break;
            case AppEventType.READY:
            default:
                loading.set(false);
                break;
        }
    }

    private void displaySnackbar(@StringRes int message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
