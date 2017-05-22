package technology.mainthread.apps.gatekeeper.service;

import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import technology.mainthread.apps.gatekeeper.data.RegisterDevices;

public class MessagingInstanceIdService extends FirebaseInstanceIdService {

    @Inject
    RegisterDevices registerDevices;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {
        registerDevices.registerDevice();
        startService(RefreshFCMSubscriptionsService.getRefreshFCMSubscriptionsIntent(this));
    }

}
