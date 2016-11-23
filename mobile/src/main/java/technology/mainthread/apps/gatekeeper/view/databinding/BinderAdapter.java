package technology.mainthread.apps.gatekeeper.view.databinding;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState;
import timber.log.Timber;

public class BinderAdapter {

    private BinderAdapter() {
    }

    @BindingAdapter({"datetime"})
    public static void setFormattedDateTime(TextView textView, String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.UK);
            Date date = sdf.parse(timestamp);
            CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(date.getTime());
            textView.setText(relativeTimeSpanString);
        } catch (ParseException e) {
            Timber.w(e, "ParseException");
        }
    }

    @BindingAdapter({"formatEvent"})
    public static void formatEvent(TextView textView, String state) {
        String prettyState;
        switch (state) {
            case "gatekeeper/setup":
                prettyState = "System online";
                break;
            case "gatekeeper/handset-activated":
                prettyState = "Handset called";
                break;
            case "gatekeeper/handset-deactivated":
                prettyState = "Handset call finished";
                break;
            case "gatekeeper/primed":
                prettyState = "Primed";
                break;
            case "gatekeeper/unprimed":
                prettyState = "Unprimed";
                break;
            case "gatekeeper/unlocked":
                prettyState = "Door unlocked";
                break;
            case "gatekeeper/door-opened":
                prettyState = "Door opened";
                break;
            case "gatekeeper/door-closed":
                prettyState = "Door closed";
                break;
            default:
                prettyState = state;
                break;
        }
        textView.setText(prettyState);
    }

    @BindingAdapter({"statusColor"})
    public static void deviceStatusIcon(View view, String state) {
        Context context = view.getContext();
        int color = ContextCompat.getColor(context, android.R.color.holo_red_dark);

        if (state != null) {
            @GatekeeperState String deviceState = state.toUpperCase();
            switch (deviceState) {
                case GatekeeperState.ONLINE:
                    color = ContextCompat.getColor(context, android.R.color.holo_green_light);
                    break;
                case GatekeeperState.DOOR_OPEN:
                    color = ContextCompat.getColor(context, android.R.color.holo_blue_light);
                    break;
                case GatekeeperState.PRIMED:
                    color = ContextCompat.getColor(context, android.R.color.holo_purple);
                    break;
                case GatekeeperState.OFFLINE:
                default:
                    break;
            }
        }
        view.setBackgroundColor(color);
    }

    @BindingAdapter({"statusText"})
    public static void deviceStatusText(TextView textView, String state) {
        Resources res = textView.getResources();
        String statusText = res.getString(R.string.status_unknown);

        if (state != null) {
            @GatekeeperState String deviceState = state.toUpperCase();
            switch (deviceState) {
                case GatekeeperState.ONLINE:
                    statusText = res.getString(R.string.status_online);
                    break;
                case GatekeeperState.DOOR_OPEN:
                    statusText = res.getString(R.string.status_door_open);
                    break;
                case GatekeeperState.PRIMED:
                    statusText = res.getString(R.string.status_primed);
                    break;
                case GatekeeperState.OFFLINE:
                    statusText = res.getString(R.string.status_offline);
                    break;
                default:
                    break;
            }
        }

        textView.setText(statusText);
    }
}
