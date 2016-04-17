package technology.mainthread.apps.gatekeeper.model.event;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({AppEventType.READY, AppEventType.CHECKING, AppEventType.PRIMING, AppEventType.UNLOCKING, AppEventType.COMPLETE})
public @interface AppEventType {
    String READY = "READY";
    String CHECKING = "CHECKING";
    String PRIMING = "PRIMING";
    String UNLOCKING = "UNLOCKING";
    String COMPLETE = "COMPLETE";
}
