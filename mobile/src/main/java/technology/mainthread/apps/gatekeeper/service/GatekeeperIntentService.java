package technology.mainthread.apps.gatekeeper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper;
import technology.mainthread.apps.gatekeeper.data.AppStateController;
import technology.mainthread.apps.gatekeeper.data.RemoteConfigKeys;
import technology.mainthread.apps.gatekeeper.data.service.GatekeeperService;
import technology.mainthread.apps.gatekeeper.data.service.RxDeviceState;
import technology.mainthread.apps.gatekeeper.model.event.AppEventType;
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceAction;
import technology.mainthread.apps.gatekeeper.view.NotifierHelper;
import timber.log.Timber;

import static technology.mainthread.apps.gatekeeper.model.event.StringDefUtil.getGatekeeperState;

public class GatekeeperIntentService extends Service {

    private static final String ACTION_CHECK_STATE = "ACTION_CHECK_STATE";
    private static final String ACTION_UNLOCK = "ACTION_UNLOCK";
    private static final String ACTION_PRIME = "ACTION_PRIME";

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

    private CompositeSubscription cs = new CompositeSubscription();
    private Handler handler = new Handler();

    public static Intent getCheckGatekeeperStateIntent(Context context) {
        Intent intent = new Intent(context, GatekeeperIntentService.class);
        intent.setAction(ACTION_CHECK_STATE);
        return intent;
    }

    public static Intent getUnlockGatekeeperIntent(Context context) {
        Intent intent = new Intent(context, GatekeeperIntentService.class);
        intent.setAction(ACTION_UNLOCK);
        return intent;
    }

    public static Intent getPrimeGatekeeperIntent(Context context) {
        Intent intent = new Intent(context, GatekeeperIntentService.class);
        intent.setAction(ACTION_PRIME);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GatekeeperApp.get(this).inject(this);
    }

    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
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

    @Override public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        cs.clear();
        super.onDestroy();
    }

    private void checkGatekeeperState() {
        Subscription subscription = rxDeviceState.gatekeeperStateObservable()
                .compose(RxSchedulerHelper.applySchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        Timber.e(e, "Throwable when unlocking");
                    }

                    @Override public void onNext(String state) {
                        appStateController.updateGatekeeperState(getGatekeeperState(state));
                        appStateController.onAppEvent(AppEventType.READY, false, 0);
                    }
                });

        cs.add(subscription);
    }

    private void checkGatekeeperStateInFuture(int millisecondsFromNow) {
        handler.postDelayed(this::checkGatekeeperState, millisecondsFromNow);
    }

    private void unlock() {
        appStateController.onAppEvent(AppEventType.UNLOCKING, false, R.string.event_unlock_in_progress);
        notifierHelper.onUnlockPressed();

        Subscription subscription = gatekeeperService.unlock(config.getString(RemoteConfigKeys.PARTICLE_AUTH))
                .compose(RxSchedulerHelper.applySchedulers())
                .subscribe(new Subscriber<Response<DeviceAction>>() {
                    @Override public void onCompleted() {
                        // NO_OP
                    }

                    @Override public void onError(Throwable e) {
                        Timber.e(e, "Throwable when unlocking");
                        appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail);
                    }

                    @Override public void onNext(Response<DeviceAction> response) {
                        if (response.isSuccessful()) {
                            if (response.body().returnValue() == 0) {
                                appStateController.onAppEvent(AppEventType.COMPLETE, true, R.string.event_unlock_success);
                                notifierHelper.notifyHandsetUnlocked(true);
                            } else {
                                appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail);
                            }
                        } else {
                            appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail);
                        }
                    }
                });
        cs.add(subscription);
    }

    private void prime() {
        appStateController.onAppEvent(AppEventType.PRIMING, false, R.string.event_prime_in_progress);
        notifierHelper.onPrimePressed();

        Subscription subscription = gatekeeperService.prime(config.getString(RemoteConfigKeys.PARTICLE_AUTH))
                .compose(RxSchedulerHelper.applySchedulers())
                .subscribe(new Subscriber<Response<DeviceAction>>() {
                    @Override public void onCompleted() {
                        // NO_OP
                    }

                    @Override public void onError(Throwable e) {
                        Timber.e(e, "Throwable when priming");
                        appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail);
                    }

                    @Override public void onNext(Response<DeviceAction> response) {
                        if (response.isSuccessful()) {
                            if (response.body().returnValue() == 0) {
                                appStateController.updateGatekeeperState(GatekeeperState.PRIMED);
                                appStateController.onAppEvent(AppEventType.COMPLETE, true, R.string.event_prime_success);
                                notifierHelper.notifySystemPrimed(true);
                                checkGatekeeperStateInFuture(TIME_UNTIL_PRIME_EXPIRED);
                            } else { // Device failure
                                appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_prime_fail);
                                notifierHelper.notifySystemPrimed(false);
                                checkGatekeeperStateInFuture(10000);
                            }
                        } else {
                            appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_prime_fail);
                            notifierHelper.notifySystemPrimed(false);
                            checkGatekeeperStateInFuture(10000);
                        }
                    }
                });
        cs.add(subscription);
    }

}
