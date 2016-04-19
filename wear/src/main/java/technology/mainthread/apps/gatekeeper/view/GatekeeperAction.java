package technology.mainthread.apps.gatekeeper.view;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({GatekeeperAction.ACTION_PRIME, GatekeeperAction.ACTION_UNLOCK})
public @interface GatekeeperAction {
    String ACTION_PRIME = "prime";
    String ACTION_UNLOCK = "unlock";
}
