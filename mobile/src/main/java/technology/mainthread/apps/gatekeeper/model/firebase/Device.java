package technology.mainthread.apps.gatekeeper.model.firebase;

public class Device {

    public String userId;

    public String deviceName;

    public String pushToken;

    public Device() {
    }

    private Device(Builder builder) {
        userId = builder.userId;
        deviceName = builder.deviceName;
        pushToken = builder.pushToken;
    }

    public static Builder Builder() {
        return new Builder();
    }

    public String getUserId() {
        return userId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getPushToken() {
        return pushToken;
    }

    @Override
    public String toString() {
        return "Device{" +
                "userId='" + userId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", pushToken='" + pushToken + '\'' +
                '}';
    }

    public static final class Builder {
        private String userId;
        private String deviceName;
        private String pushToken;

        private Builder() {
        }

        public Builder userId(String val) {
            userId = val;
            return this;
        }

        public Builder deviceName(String val) {
            deviceName = val;
            return this;
        }

        public Builder pushToken(String val) {
            pushToken = val;
            return this;
        }

        public Device build() {
            return new Device(this);
        }
    }
}
