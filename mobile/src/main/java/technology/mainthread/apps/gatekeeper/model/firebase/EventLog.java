package technology.mainthread.apps.gatekeeper.model.firebase;

public class EventLog {

    private String name;
    private long timestamp;
    private String source;
    private String value;

    public EventLog() {
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp * -1L;
    }

    public String getSource() {
        return source;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "EventLog{" +
                "name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", source='" + source + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
