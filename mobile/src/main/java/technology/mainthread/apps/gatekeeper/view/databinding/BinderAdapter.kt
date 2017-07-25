package technology.mainthread.apps.gatekeeper.view.databinding

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState
import technology.mainthread.apps.gatekeeper.model.event.getGatekeeperState
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@JvmName("BinderAdapter")

@BindingAdapter("datetime")
fun setFormattedDateTime(textView: TextView, timestamp: String) {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.UK)
        val date = sdf.parse(timestamp)
        val relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(date.time)
        textView.text = relativeTimeSpanString
    } catch (e: ParseException) {
        Timber.w(e, "ParseException")
    }

}

@BindingAdapter("formatEvent")
fun formatEvent(textView: TextView, state: String) {
    val prettyState: String
    when (state) {
        "gatekeeper/setup" -> prettyState = "System online"
        "gatekeeper/handset-activated" -> prettyState = "Handset called"
        "gatekeeper/handset-deactivated" -> prettyState = "Handset call finished"
        "gatekeeper/primed" -> prettyState = "Primed"
        "gatekeeper/unprimed" -> prettyState = "Unprimed"
        "gatekeeper/unlocked" -> prettyState = "Door unlocked"
        "gatekeeper/door-opened" -> prettyState = "Door opened"
        "gatekeeper/door-closed" -> prettyState = "Door closed"
        else -> prettyState = state
    }
    textView.text = prettyState
}

@BindingAdapter("statusColor")
fun deviceStatusIcon(view: View, state: String?) {
    val context = view.context
    var color = ContextCompat.getColor(context, android.R.color.holo_red_dark)

    if (state != null) {
        val deviceState = getGatekeeperState(state.toUpperCase())
        when (deviceState) {
            GatekeeperState.ONLINE -> color = ContextCompat.getColor(context, android.R.color.holo_green_light)
            GatekeeperState.DOOR_OPEN -> color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
            GatekeeperState.PRIMED -> color = ContextCompat.getColor(context, android.R.color.holo_purple)
            else -> {
            }
        }
    }
    view.setBackgroundColor(color)
}

@BindingAdapter("statusText")
fun deviceStatusText(textView: TextView, state: String?) {
    val res = textView.resources
    var statusText = res.getString(R.string.status_unknown)

    if (state != null) {
        val deviceState = getGatekeeperState(state.toUpperCase())
        when (deviceState) {
            GatekeeperState.ONLINE -> statusText = res.getString(R.string.status_online)
            GatekeeperState.DOOR_OPEN -> statusText = res.getString(R.string.status_door_open)
            GatekeeperState.PRIMED -> statusText = res.getString(R.string.status_primed)
            GatekeeperState.OFFLINE -> statusText = res.getString(R.string.status_offline)
            else -> {
            }
        }
    }

    textView.text = statusText
}
