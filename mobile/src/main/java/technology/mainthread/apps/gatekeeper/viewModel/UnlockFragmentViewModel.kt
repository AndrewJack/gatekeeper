package technology.mainthread.apps.gatekeeper.viewModel

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import android.view.View
import io.reactivex.disposables.Disposable
import technology.mainthread.apps.gatekeeper.rx.applyObservableSchedulers
import technology.mainthread.apps.gatekeeper.data.AppStateController
import technology.mainthread.apps.gatekeeper.model.event.AppEvent
import technology.mainthread.apps.gatekeeper.model.event.AppEventType
import technology.mainthread.apps.gatekeeper.service.ACTION_CHECK_STATE
import technology.mainthread.apps.gatekeeper.service.ACTION_PRIME
import technology.mainthread.apps.gatekeeper.service.ACTION_UNLOCK
import technology.mainthread.apps.gatekeeper.service.getGatekeeperStateIntent

class UnlockFragmentViewModel(private val context: Context,
                              private val rootView: View,
                              private val appStateController: AppStateController) : BaseObservable() {

    var loading = ObservableBoolean()

    var deviceStatus = ObservableField<String>()

    private var disposable: Disposable? = null

    /**
     * Setup the device status thing.
     */
    fun initialize() {
        disposable = appStateController.observable
                .compose(applyObservableSchedulers<AppEvent>())
                .subscribe { event -> onAppEvent(event) }

        deviceStatus.set(appStateController.lastKnownGatekeeperState.label)
        context.startService(getGatekeeperStateIntent(context, ACTION_CHECK_STATE))
    }

    fun dispose() {
        disposable?.dispose()
    }

    fun onUnlockClicked(view: View) {
        context.startService(getGatekeeperStateIntent(context, ACTION_UNLOCK))
    }

    fun onPrimeClicked(view: View) {
        context.startService(getGatekeeperStateIntent(context, ACTION_PRIME))
    }

    private fun onAppEvent(appEvent: AppEvent) {
        deviceStatus.set(appEvent.gatekeeperState.label)

        when (appEvent.appState) {
            AppEventType.CHECKING, AppEventType.UNLOCKING, AppEventType.PRIMING -> loading.set(true)
            AppEventType.COMPLETE -> {
                loading.set(false)
                displaySnackbar(appEvent.message)
            }
            AppEventType.READY -> loading.set(false)
        }
    }

    private fun displaySnackbar(@StringRes message: Int) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }
}
