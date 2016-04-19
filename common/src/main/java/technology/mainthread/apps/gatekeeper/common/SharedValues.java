package technology.mainthread.apps.gatekeeper.common;

public class SharedValues {

    private SharedValues() {
    }

    // TIMEOUT
    public static final long CONNECTION_TIME_OUT_MS = 30 * 1000;

    // PATHS
    public static final String PATH_WEAR_ERROR = "/gatekeeper/wear/wear_error";
    public static final String PATH_PRIME = "/gatekeeper/prime";
    public static final String PATH_UNLOCK = "/gatekeeper/unlock";

    // KEYS
    public static final String KEY_WEAR_EXCEPTION = "KEY_WEAR_EXCEPTION";

}
