package technology.mainthread.apps.gatekeeper.model.event

enum class AppEventType(val label: String) {
    READY("READY"),
    CHECKING("CHECKING"),
    PRIMING("PRIMING"),
    UNLOCKING("UNLOCKING"),
    COMPLETE("COMPLETE")
}
