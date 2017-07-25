package technology.mainthread.apps.gatekeeper.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import technology.mainthread.apps.gatekeeper.view.notificaton.NotifierHelper
import timber.log.Timber
import javax.inject.Inject

class MessagingService : FirebaseMessagingService() {

    @Inject
    internal lateinit var notifierHelper: NotifierHelper

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Timber.d("From: %s", remoteMessage!!.from)
        for (key in remoteMessage.data.keys) {
            Timber.d("%1\$s is a key in the bundle, %2\$s is the value", key, remoteMessage.data[key])
        }

        notifierHelper.handlePushNotification(remoteMessage.from)
    }
}
