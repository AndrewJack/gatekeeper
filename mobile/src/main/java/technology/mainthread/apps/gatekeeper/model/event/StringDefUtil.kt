package technology.mainthread.apps.gatekeeper.model.event

fun getGatekeeperState(state: String?): GatekeeperState {
    if (state != null) {
        val gatekeeperState = state.toUpperCase()
        when (gatekeeperState) {
            GatekeeperState.OFFLINE.label -> return GatekeeperState.OFFLINE
            GatekeeperState.ONLINE.label -> return GatekeeperState.ONLINE
            GatekeeperState.DOOR_OPEN.label -> return GatekeeperState.DOOR_OPEN
            GatekeeperState.PRIMED.label -> return GatekeeperState.PRIMED
            else -> {
            }
        }
    }
    return GatekeeperState.UNKNOWN
}
