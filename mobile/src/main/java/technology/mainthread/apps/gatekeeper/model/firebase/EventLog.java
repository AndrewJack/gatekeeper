package technology.mainthread.apps.gatekeeper.model.firebase;

public class EventLog {

    private String name;
    private String source;
    private String value;
    private String published;

    public EventLog() {
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getValue() {
        return value;
    }

    public String getPublished() {
        return published;
    }

    @Override
    public String toString() {
        return "EventLog{" +
                "name='" + name + '\'' +
                ", source='" + source + '\'' +
                ", value='" + value + '\'' +
                ", published='" + published + '\'' +
                '}';
    }

}
