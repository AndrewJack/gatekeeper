package technology.mainthread.apps.gatekeeper.model.firebase;

public class EventLog {

    public String name;

    public String published;

    public Double sort;

    public String source;

    public EventLog() {
    }

    private EventLog(Builder builder) {
        name = builder.name;
        published = builder.published;
        sort = builder.sort;
        source = builder.source;
    }

    public static Builder Builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getPublished() {
        return published;
    }

    public Double getSort() {
        return sort;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "EventLog{" +
                "name='" + name + '\'' +
                ", published='" + published + '\'' +
                ", sort=" + sort +
                ", source='" + source + '\'' +
                '}';
    }

    public static final class Builder {
        private String name;
        private String published;
        private Double sort;
        private String source;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder published(String val) {
            published = val;
            return this;
        }

        public Builder sort(Double val) {
            sort = val;
            return this;
        }

        public Builder source(String val) {
            source = val;
            return this;
        }

        public EventLog build() {
            return new EventLog(this);
        }
    }
}
