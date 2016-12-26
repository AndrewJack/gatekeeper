package technology.mainthread.apps.gatekeeper.service;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.common.SharedValues;
import timber.log.Timber;

import static technology.mainthread.apps.gatekeeper.service.GatekeeperStateService.ACTION_PRIME;
import static technology.mainthread.apps.gatekeeper.service.GatekeeperStateService.ACTION_UNLOCK;
import static technology.mainthread.apps.gatekeeper.service.GatekeeperStateService.getGatekeeperStateIntent;

public class MobileWearListenerService extends WearableListenerService {

    @Inject
    @Named("wear")
    GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        GatekeeperApp.get(this).inject(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath() != null) {
            switch (messageEvent.getPath()) {
                case SharedValues.PATH_WEAR_ERROR:
                    logWearException(messageEvent);
                    break;
                case SharedValues.PATH_PRIME:
                    startService(getGatekeeperStateIntent(this, ACTION_PRIME));
                    break;
                case SharedValues.PATH_UNLOCK:
                    startService(getGatekeeperStateIntent(this, ACTION_UNLOCK));
                    break;
                default:
                    Timber.d("onMessageReceived: %s", messageEvent.getPath());
                    break;
            }
        }
    }

    private void logWearException(MessageEvent messageEvent) {
        if (!googleApiClient.blockingConnect(SharedValues.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS).isSuccess()) {
            return;
        }

        DataMap map = DataMap.fromByteArray(messageEvent.getData());

        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(map.getByteArray(SharedValues.KEY_WEAR_EXCEPTION));
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            Throwable ex = (Throwable) ois.readObject();
            Timber.w(ex, "Android wear exception");

            Crashlytics.setBool("wear_exception", true);
            Crashlytics.setString("board", map.getString("board"));
            Crashlytics.setString("fingerprint", map.getString("fingerprint"));
            Crashlytics.setString("model", map.getString("model"));
            Crashlytics.setString("manufacturer", map.getString("manufacturer"));
            Crashlytics.setString("product", map.getString("product"));
            Crashlytics.logException(ex);

        } catch (IOException | ClassNotFoundException e) {
            Timber.e(e, "track exception failed");
        }
    }

}
