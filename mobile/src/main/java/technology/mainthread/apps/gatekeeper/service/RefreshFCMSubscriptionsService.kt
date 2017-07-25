package technology.mainthread.apps.gatekeeper.service

import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessaging
import dagger.android.DaggerIntentService
import io.reactivex.Observable
import technology.mainthread.apps.gatekeeper.R
import javax.inject.Inject

class RefreshFCMSubscriptionsService : DaggerIntentService(RefreshFCMSubscriptionsService::class.java.simpleName) {

    @Inject
    internal lateinit var messaging: FirebaseMessaging

    override fun onHandleIntent(intent: Intent) {
        if (ACTION_REFRESH_FCM_SUBSCRIPTIONS == intent.action) {
            refreshSubscriptions()
        }
    }

    private fun refreshSubscriptions() {
        val allSubscriptions = resources.getStringArray(R.array.notif_subscriptions_values)
        Observable.fromArray(*allSubscriptions).forEach { subscription -> messaging.subscribeToTopic(subscription) }
    }

    companion object {

        private val ACTION_REFRESH_FCM_SUBSCRIPTIONS = "ACTION_REFRESH_FCM_SUBSCRIPTIONS"

        fun getRefreshFCMSubscriptionsIntent(context: Context): Intent {
            val intent = Intent(context, RefreshFCMSubscriptionsService::class.java)
            intent.action = ACTION_REFRESH_FCM_SUBSCRIPTIONS
            return intent
        }
    }
}
