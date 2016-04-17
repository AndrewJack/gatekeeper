package technology.mainthread.apps.gatekeeper.model.event;

import android.support.annotation.NonNull;

public class StringDefUtil {

    private StringDefUtil() {
    }

    public static @NonNull @GatekeeperState String getGatekeeperState(String state) {
        if (state != null) {
            String gatekeeperState = state.toUpperCase();
            switch (gatekeeperState) {
                case GatekeeperState.OFFLINE:
                    return GatekeeperState.OFFLINE;
                case GatekeeperState.ONLINE:
                    return GatekeeperState.ONLINE;
                case GatekeeperState.DOOR_OPEN:
                    return GatekeeperState.DOOR_OPEN;
                case GatekeeperState.PRIMED:
                    return GatekeeperState.PRIMED;
                default:
                    break;
            }
        }
        return GatekeeperState.UNKNOWN;
    }

}
