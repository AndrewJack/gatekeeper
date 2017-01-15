package technology.mainthread.apps.gatekeeper.service;

import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.data.RegisterDevices;

public class MessagingInstanceIdService extends FirebaseInstanceIdService {

    @Inject
    RegisterDevices registerDevices;

    @Override
    public void onCreate() {
        super.onCreate();
        GatekeeperApp.get(this).inject(this);
    }

    @Override
    public void onTokenRefresh() {
        registerDevices.registerDevice();
        startService(RefreshFCMSubscriptionsService.getRefreshFCMSubscriptionsIntent(this));
    }

}
