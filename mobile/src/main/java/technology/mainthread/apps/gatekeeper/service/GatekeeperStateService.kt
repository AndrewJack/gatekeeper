package technology.mainthread.apps.gatekeeper.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.android.DaggerService
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.rx.applyFlowableSchedulers
import technology.mainthread.apps.gatekeeper.data.AppStateController
import technology.mainthread.apps.gatekeeper.data.PARTICLE_AUTH
import technology.mainthread.apps.gatekeeper.data.service.GatekeeperService
import technology.mainthread.apps.gatekeeper.data.service.RxDeviceState
import technology.mainthread.apps.gatekeeper.model.event.AppEventType
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState
import technology.mainthread.apps.gatekeeper.model.particle.DeviceAction
import technology.mainthread.apps.gatekeeper.view.notificaton.NotifierHelper
import timber.log.Timber
import javax.inject.Inject

class GatekeeperStateService : DaggerService() {

    @Inject
    internal lateinit var gatekeeperService: GatekeeperService
    @Inject
    internal lateinit var rxDeviceState: RxDeviceState
    @Inject
    internal lateinit var notifierHelper: NotifierHelper
    @Inject
    internal lateinit var appStateController: AppStateController
    @Inject
    internal lateinit var config: FirebaseRemoteConfig

    private val cs = CompositeDisposable()
    private val handler = Handler()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action

        if (ACTION_CHECK_STATE == action) {
            checkGatekeeperState()
        } else if (ACTION_UNLOCK == action) {
            unlock()
        } else if (ACTION_PRIME == action) {
            prime()
        }
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        cs.clear()
        super.onDestroy()
    }

    private fun checkGatekeeperState() {
        val disposable = rxDeviceState.gatekeeperStateObservable()
                .compose(applyFlowableSchedulers<GatekeeperState>())
                .subscribe({ state ->
                    appStateController.updateGatekeeperState(state)
                    appStateController.onAppEvent(AppEventType.READY, false, 0)
                }) { t -> Timber.e(t, "Throwable when unlocking") }

        cs.add(disposable)
    }

    private fun checkGatekeeperStateInFuture(millisecondsFromNow: Int) {
        handler.postDelayed({ this.checkGatekeeperState() }, millisecondsFromNow.toLong())
    }

    private fun unlock() {
        appStateController.onAppEvent(AppEventType.UNLOCKING, false, R.string.event_unlock_in_progress)
        notifierHelper.onUnlockPressed()

        val disposable = gatekeeperService.unlock(config.getString(PARTICLE_AUTH))
                .compose<Response<DeviceAction>>(applyFlowableSchedulers<Response<DeviceAction>>())
                .subscribe({ deviceActionResponse ->
                    if (deviceActionResponse.isSuccessful()) {
                        if (deviceActionResponse.body()?.returnValue == 0) {
                            appStateController.onAppEvent(AppEventType.COMPLETE, true, R.string.event_unlock_success)
                            notifierHelper.notifyHandsetUnlocked(true)
                        } else {
                            appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail)
                        }
                    } else {
                        appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail)
                    }
                }) { t ->
                    Timber.e(t, "Throwable when unlocking")
                    appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail)
                }
        cs.add(disposable)
    }

    private fun prime() {
        appStateController.onAppEvent(AppEventType.PRIMING, false, R.string.event_prime_in_progress)

        val disposable = gatekeeperService.prime(config.getString(PARTICLE_AUTH))
                .compose<Response<DeviceAction>>(applyFlowableSchedulers<Response<DeviceAction>>())
                .subscribe({ deviceActionResponse ->
                    if (deviceActionResponse.isSuccessful()) {
                        if (deviceActionResponse.body()?.returnValue == 0) {
                            appStateController.updateGatekeeperState(GatekeeperState.PRIMED)
                            appStateController.onAppEvent(AppEventType.COMPLETE, true, R.string.event_prime_success)
                            checkGatekeeperStateInFuture(TIME_UNTIL_PRIME_EXPIRED)
                        } else { // Device failure
                            appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_prime_fail)
                            checkGatekeeperStateInFuture(10000)
                        }
                    } else {
                        appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_prime_fail)
                        checkGatekeeperStateInFuture(10000)
                    }
                }) { t ->
                    Timber.e(t, "Throwable when priming")
                    appStateController.onAppEvent(AppEventType.COMPLETE, false, R.string.event_unlock_fail)
                }
        cs.add(disposable)
    }

}

const val ACTION_CHECK_STATE = "ACTION_CHECK_STATE"
const val ACTION_UNLOCK = "ACTION_UNLOCK"
const val ACTION_PRIME = "ACTION_PRIME"

private val TIME_UNTIL_PRIME_EXPIRED = 2 * 60 * 1000 // Default 2 mins // TODO: fetch from api

fun getGatekeeperStateIntent(context: Context, action: String): Intent {
    val intent = Intent(context, GatekeeperStateService::class.java)
    intent.action = action
    return intent
}
