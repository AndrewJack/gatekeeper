package technology.mainthread.apps.gatekeeper.model.event;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({GatekeeperState.ONLINE, GatekeeperState.OFFLINE, GatekeeperState.PRIMED,
        GatekeeperState.DOOR_OPEN, GatekeeperState.UNKNOWN})
public @interface GatekeeperState {
    String ONLINE = "ONLINE";
    String OFFLINE = "OFFLINE";
    String DOOR_OPEN = "DOOR_OPEN";
    String PRIMED = "PRIMED";
    String UNKNOWN = "UNKNOWN";
}
