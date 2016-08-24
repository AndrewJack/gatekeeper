package technology.mainthread.apps.gatekeeper.model.particle;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class DeviceAction {

    @Json(name = "return_value") public abstract int returnValue();

    public abstract boolean connected();

    public static JsonAdapter<DeviceAction> jsonAdapter(Moshi moshi) {
        return new AutoValue_DeviceAction.MoshiJsonAdapter(moshi);
    }

    public static Builder builder() {
        return new AutoValue_DeviceAction.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder returnValue(int value);

        public abstract Builder connected(boolean value);

        public abstract DeviceAction build();
    }

}
