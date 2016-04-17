package technology.mainthread.apps.gatekeeper.service.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.view.NotifierHelper;
import timber.log.Timber;

public class GatekeeperGcmListenerService extends GcmListenerService {

    @Inject
    NotifierHelper notifierHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        GatekeeperApp.get(this).inject(this);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Timber.d("From: %s", from);
        for (String key : data.keySet()) {
            Timber.d("%1$s is a key in the bundle, %2$s is the value", key, data.getString(key));
        }

        notifierHelper.notifyHandsetCalling();
    }
}
