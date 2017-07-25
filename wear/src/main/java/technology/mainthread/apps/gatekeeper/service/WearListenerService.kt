package technology.mainthread.apps.gatekeeper.service

import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import timber.log.Timber

class WearListenerService : WearableListenerService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDataChanged(dataEvents: DataEventBuffer?) {
        Timber.d("onDataChanged: %s", dataEvents)
    }

    override fun onMessageReceived(messageEvent: MessageEvent?) {
        Timber.d("onMessageReceived: %s", messageEvent)
    }
}
