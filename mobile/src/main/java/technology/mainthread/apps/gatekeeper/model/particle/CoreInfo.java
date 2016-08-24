package technology.mainthread.apps.gatekeeper.model.particle;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class CoreInfo {

    public abstract boolean connected();
    @Json(name = "last_heard") public abstract String lastHeard();

    public static JsonAdapter<CoreInfo> jsonAdapter(Moshi moshi) {
        return new AutoValue_CoreInfo.MoshiJsonAdapter(moshi);
    }

    public static Builder builder() {
        return new AutoValue_CoreInfo.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder connected(boolean value);
        public abstract Builder lastHeard(String value);
        public abstract CoreInfo build();
    }

}
