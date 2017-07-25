package technology.mainthread.apps.gatekeeper.service

import com.google.firebase.iid.FirebaseInstanceIdService
import dagger.android.AndroidInjection
import technology.mainthread.apps.gatekeeper.data.RegisterDevices
import javax.inject.Inject

class MessagingInstanceIdService : FirebaseInstanceIdService() {

    @Inject
    internal lateinit var registerDevices: RegisterDevices

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onTokenRefresh() {
        registerDevices.registerDevice()
        startService(RefreshFCMSubscriptionsService.getRefreshFCMSubscriptionsIntent(this))
    }

}
