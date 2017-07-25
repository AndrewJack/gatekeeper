package technology.mainthread.apps.gatekeeper.view.notificaton

import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.data.*
import technology.mainthread.apps.gatekeeper.service.ACTION_UNLOCK
import technology.mainthread.apps.gatekeeper.service.getGatekeeperStateIntent
import technology.mainthread.apps.gatekeeper.view.activity.FRAG_LOGS
import technology.mainthread.apps.gatekeeper.view.activity.getMainIntent
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotifierHelper @Inject
constructor(private val context: Context, private val resources: Resources, private val notificationManager: NotificationManagerCompat, private val preferences: SharedPreferences) {

    private val ID_HANDSET_CALLING = 1
    private val ID_UNLOCKED = 2
    private val ID_HANDSET_CALLED = 3
    private val ID_OTHER = 20

    private val handler: Handler = Handler()

    // Cancels calling notification
    private val cancelNotificationRunnable = Runnable {
        notificationManager.cancel(ID_HANDSET_CALLING)
        notifyHandsetCalled()
    }

    // Public methods

    fun handlePushNotification(from: String?) {
        val topic = from?.replace("/topics/", "") ?: ""
        if (!shouldDisplayNotification(topic)) {
            return
        }

        when (topic) {
            HANDSET_ACTIVATED -> notifyHandsetCalling()
            HANDSET_DEACTIVATED -> {
                clearCallingNotification()
                notifyHandsetCalled()
            }
            UNLOCKED, DOOR_OPENED, DOOR_CLOSED, PRIMED, UNPRIMED -> notifyOther(topic)
            else -> {
            }
        }
    }

    /**
     * To be called when the unlock button has been pressed
     * Cancels calling notification
     * Then removes callback for dismissing and displaying the called notification
     */
    fun onUnlockPressed() {
        clearCallingNotification()
    }

    /**
     * Display notification when a response from the unlock service has been received

     * @param success whether the unlock request was successful
     */
    fun notifyHandsetUnlocked(success: Boolean) {
        val title: String
        val text: String

        if (success) {
            title = resources.getString(R.string.notif_title_unlock_success)
            text = resources.getString(R.string.notif_text_unlock_success)
        } else {
            title = resources.getString(R.string.notif_title_unlock_failed)
            text = resources.getString(R.string.notif_text_unlock_failed)

        }

        val notification = baseNotification
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(logsPendingIntent)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(ID_UNLOCKED, notification)
    }

    // \Public methods

    // Private methods

    private fun notifyHandsetCalling() {
        val unlockPendingIntent = PendingIntent.getService(context, 0, getGatekeeperStateIntent(context, ACTION_UNLOCK), PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = baseNotification
                .setContentTitle(resources.getString(R.string.notif_title_handset_ringing))
                .setContentIntent(logsPendingIntent)
                .setVibrate(vibrationPreference)
                .setSound(alarmSound)
                .addAction(R.drawable.ic_lock_open_black_24dp,
                        resources.getString(R.string.notif_action_title_unlock), unlockPendingIntent)
                .setAutoCancel(true)

        notificationManager.notify(ID_HANDSET_CALLING, notification.build())

        handler.postDelayed(cancelNotificationRunnable, 60000) // run after a minute
    }

    private fun notifyHandsetCalled() {
        val contentPendingIntent = PendingIntent.getActivity(context, 0, getMainIntent(context, FRAG_LOGS), 0)

        val notification = baseNotification
                .setContentTitle("Handset was called")
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)

        notificationManager.notify(ID_HANDSET_CALLED, notification.build())
    }

    private fun notifyOther(topic: String) {
        val notification = baseNotification
                .setContentTitle(String.format("Gatekeeper was %s", topic))
                .setContentIntent(logsPendingIntent)
                .setAutoCancel(true)

        notificationManager.notify(ID_OTHER, notification.build())
    }

    private fun clearCallingNotification() {
        notificationManager.cancel(ID_HANDSET_CALLING)
        handler.removeCallbacks(cancelNotificationRunnable)
    }

    private val baseNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_lock_open_black_24dp)

    private val logsPendingIntent: PendingIntent
        get() = PendingIntent.getActivity(context, 0, getMainIntent(context, FRAG_LOGS), 0)

    private val alarmSound: Uri
        get() = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    private val vibrationPreference: LongArray
        get() {
            val prefVibrateTune = preferences.getString("pref_vibrate_tune", null)
            return VibratorTunes.VIBRATE_TUNES[prefVibrateTune] ?: VibratorTunes.STANDARD
        }

    private fun shouldDisplayNotification(topic: String): Boolean {
        return preferences.getStringSet("pref_notif_subscriptions", HashSet<String>()).contains(topic)
    }

}
