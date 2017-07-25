package technology.mainthread.apps.gatekeeper.data

import android.support.annotation.StringRes

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable
import technology.mainthread.apps.gatekeeper.model.event.AppEvent
import technology.mainthread.apps.gatekeeper.model.event.AppEventType
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState
import timber.log.Timber

@Singleton
class AppStateController @Inject
constructor(private val bus: RxBus) {

    var lastKnownGatekeeperState = GatekeeperState.UNKNOWN
        private set

    fun updateGatekeeperState(newState: GatekeeperState) {
        lastKnownGatekeeperState = newState
    }

    fun onAppEvent(appState: AppEventType, success: Boolean, @StringRes message: Int) {
        Timber.d("onAppStateChanged: appState: %1\$s, gatekeeperState: %2\$s, success: %3\$s, message: %4\$s, ",
                appState, lastKnownGatekeeperState, success, message)
        bus.send(AppEvent(appState, lastKnownGatekeeperState, message, success))
    }

    val observable: Observable<AppEvent>
        get() = bus.toObservable()
                .filter { o -> o is AppEvent }
                .map { o -> o as AppEvent }

}
