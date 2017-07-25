package technology.mainthread.apps.gatekeeper.service

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import dagger.android.AndroidInjection
import technology.mainthread.apps.gatekeeper.common.*
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class MobileWearListenerService : WearableListenerService() {

    @field:[Inject Named("wear")]
    internal lateinit var googleApiClient: GoogleApiClient

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path != null) {
            when (messageEvent.path) {
                PATH_WEAR_ERROR -> logWearException(messageEvent)
                PATH_PRIME -> startService(getGatekeeperStateIntent(this, ACTION_PRIME))
                PATH_UNLOCK -> startService(getGatekeeperStateIntent(this, ACTION_UNLOCK))
                else -> Timber.d("onMessageReceived: %s", messageEvent.path)
            }
        }
    }

    private fun logWearException(messageEvent: MessageEvent) {
        if (!googleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS).isSuccess) {
            return
        }

        val map = DataMap.fromByteArray(messageEvent.data)

        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }

        val bis = ByteArrayInputStream(map.getByteArray(KEY_WEAR_EXCEPTION))
        try {
            val ois = ObjectInputStream(bis)
            val ex = ois.readObject() as Throwable
            Timber.w(ex, "Android wear exception")

            //            Crashlytics.setBool("wear_exception", true);
            //            Crashlytics.setString("board", map.getString("board"));
            //            Crashlytics.setString("fingerprint", map.getString("fingerprint"));
            //            Crashlytics.setString("model", map.getString("model"));
            //            Crashlytics.setString("manufacturer", map.getString("manufacturer"));
            //            Crashlytics.setString("product", map.getString("product"));
            //            Crashlytics.logException(ex);

        } catch (e: IOException) {
            Timber.e(e, "track exception failed")
        } catch (e: ClassNotFoundException) {
            Timber.e(e, "track exception failed")
        }

    }

}
