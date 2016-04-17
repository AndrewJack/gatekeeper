package technology.mainthread.apps.gatekeeper.model.particle;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class DeviceAction {

    @Json(name = "return_value") public abstract int returnValue();

    public abstract boolean connected();

    public static JsonAdapter.Factory typeAdapter() {
        return AutoValue_DeviceAction.typeAdapterFactory();
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
