package technology.mainthread.apps.gatekeeper.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.view.NotifierHelper;
import timber.log.Timber;

public class MessagingService extends FirebaseMessagingService {

    @Inject
    NotifierHelper notifierHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        GatekeeperApp.get(this).inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d("From: %s", remoteMessage.getFrom());
        for (String key : remoteMessage.getData().keySet()) {
            Timber.d("%1$s is a key in the bundle, %2$s is the value", key, remoteMessage.getData().get(key));
        }

        notifierHelper.notifyHandsetCalling();
    }
}
