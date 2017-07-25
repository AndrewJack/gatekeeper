package technology.mainthread.apps.gatekeeper.model.event

import android.support.annotation.StringRes

data class AppEvent(val appState: AppEventType, val gatekeeperState: GatekeeperState, @StringRes val message: Int, val success: Boolean)