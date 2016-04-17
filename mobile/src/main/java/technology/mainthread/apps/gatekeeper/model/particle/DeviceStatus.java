package technology.mainthread.apps.gatekeeper.model.particle;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;

@AutoValue
public abstract class DeviceStatus {

    public abstract int result();
    public abstract CoreInfo coreInfo();

    public static JsonAdapter.Factory typeAdapter() {
        return AutoValue_DeviceStatus.typeAdapterFactory();
    }

    public static Builder builder() {
        return new AutoValue_DeviceStatus.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder result(int value);
        public abstract Builder coreInfo(CoreInfo value);
        public abstract DeviceStatus build();
    }
}
