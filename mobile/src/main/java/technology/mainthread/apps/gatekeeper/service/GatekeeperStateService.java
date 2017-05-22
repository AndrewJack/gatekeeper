package technology.mainthread.apps.gatekeeper.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper;
import technology.mainthread.apps.gatekeeper.data.AppStateController;
import technology.mainthread.apps.gatekeeper.data.RemoteConfigKeys;
import technology.mainthread.apps.gatekeeper.data.service.GatekeeperService;
import technology.mainthread.apps.gatekeeper.data.service.RxDeviceState;
import technology.mainthread.apps.gatekeeper.model.event.AppEventType;
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState;
import technology.mainthread.apps.gatekeeper.view.notificaton.NotifierHelper;
import timber.log.Timber;

import static technology.mainthread.apps.gatekeeper.model.event.StringDefUtil.getGatekeeperState;

public class GatekeeperStateService extends DaggerService {

    public static final String ACTION_CHECK_STATE = "ACTION_CHECK_STATE";
    public static final String ACTION_UNLOCK = "ACTION_UNLOCK";
    public static final String ACTION_PRIME = "ACTION_PRIME";

    private static final int TIME_UNTIL_PRIME_EXPIRED = 2 * 60 * 1000; // Default 2 mins // TODO: fetch from api

    @Inject
    GatekeeperService gatekeeperService;
    @Inject
    RxDeviceState rxDeviceState;
    @Inject
    NotifierHelper notifierHelper;
    @Inject
    AppStateController appStateController;
    @Inject
    FirebaseRemoteConfig config;

    private CompositeDisposable cs = new CompositeDisposable();
    private Handler handler = new Handler();

    public static Intent getGatekeeperStateIntent(Context context, String action) {
        Intent intent = new Intent(context, GatekeeperStateService.class);
        intent.setAction(action);
        return intent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (ACTION_CHECK_STATE.equals(action)) {
            checkGatekeeperState();
        } else if (ACTION_UNLOCK.equals(action)) {
            unlock();
        } else if (ACTION_PRIME.equals(action)) {
            prime();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        cs.clear();
        super.onDestroy();
    }

    private void checkGatekeeperState() {
        Disposable disposable = rxDeviceState.gatekeeperStateObservable()
                .compose(RxSchedulerHelper.applyFlowableSchedulers())
                .subscribe(state -> {
                    appStateController.updateGatekeeperState(getGatekeeperState(state));
                    appStateController.onAppEvent(AppEventType.READY, false, 0);
                }, t -> Timber.e(t, "Throwable when unlocking"));

        cs.add(disposable);
    }

    private void checkGatekeeperStateInFuture(int millisecondsFromNow) {
        handler.postDelayed(this::checkGatekeeperState, millisecondsFromNow);
    }

    private void unlock() {
        appStateController.onAppEvent(AppEventType.UNLOCKING, false, R.string.event_unlock_in_progress);
        notifierHelper.onUnlockPressed();

        Disposable disposable = gatekeeperService.unlock(config.getString(RemoteConfigKeys.PARTICLE_AUTH))
                .compose(RxSchedulerHelper.applyFlowableSchedulers())
                .subscribe(deviceActionResponse -> {
                    if (deviceActionResponse.isSuccessful()) {
                        if (deviceActionResponse.body().returnValue() == 0) {
                            appStateController.onAppEvent(AppEventType.COMPLETE, true, R.string.event_unlock_success);
                            notifierHelper.notifyHandsetUnlocked(true);
                        } else {
                            appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail);
                        }
                    } else {
                        appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail);
                    }
                }, t -> {
                    Timber.e(t, "Throwable when unlocking");
                    appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail);
                });
        cs.add(disposable);
    }

    private void prime() {
        appStateController.onAppEvent(AppEventType.PRIMING, false, R.string.event_prime_in_progress);

        Disposable disposable = gatekeeperService.prime(config.getString(RemoteConfigKeys.PARTICLE_AUTH))
                .compose(RxSchedulerHelper.applyFlowableSchedulers())
                .subscribe(deviceActionResponse -> {
                    if (deviceActionResponse.isSuccessful()) {
                        if (deviceActionResponse.body().returnValue() == 0) {
                            appStateController.updateGatekeeperState(GatekeeperState.PRIMED);
                            appStateController.onAppEvent(AppEventType.COMPLETE, true, R.string.event_prime_success);
                            checkGatekeeperStateInFuture(TIME_UNTIL_PRIME_EXPIRED);
                        } else { // Device failure
                            appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_prime_fail);
                            checkGatekeeperStateInFuture(10000);
                        }
                    } else {
                        appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_prime_fail);
                        checkGatekeeperStateInFuture(10000);
                    }
                }, t -> {
                    Timber.e(t, "Throwable when priming");
                    appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail);
                });
        cs.add(disposable);
    }

}
