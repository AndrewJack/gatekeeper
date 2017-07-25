package technology.mainthread.apps.gatekeeper.model.event

enum class GatekeeperState(val label: String) {
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE"),
    DOOR_OPEN("DOOR_OPEN"),
    PRIMED("PRIMED"),
    UNKNOWN("UNKNOWN")
}