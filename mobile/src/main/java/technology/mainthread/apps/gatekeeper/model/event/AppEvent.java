package technology.mainthread.apps.gatekeeper.model.event;

import android.support.annotation.StringRes;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AppEvent {

    public abstract @AppEventType String appState();

    public abstract @GatekeeperState String gatekeeperState();

    public abstract @StringRes int message();

    public abstract boolean success();

    public static Builder builder() {
        return new AutoValue_AppEvent.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder appState(@AppEventType String value);
        public abstract Builder gatekeeperState(@GatekeeperState String value);
        public abstract Builder message(@StringRes int value);
        public abstract Builder success(boolean value);
        public abstract AppEvent build();
    }

}
