package technology.mainthread.apps.gatekeeper.model.event

import androidx.annotation.StringRes

data class AppEvent(val appState: AppEventType, val gatekeeperState: GatekeeperState, @StringRes val message: Int, val success: Boolean)