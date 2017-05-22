package technology.mainthread.apps.gatekeeper.service;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import technology.mainthread.apps.gatekeeper.injector.WearAppClient;
import timber.log.Timber;

public class WearListenerService extends WearableListenerService {

    @Inject
    @WearAppClient
    GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Timber.d("onDataChanged: %s", dataEvents);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Timber.d("onMessageReceived: %s", messageEvent);
    }
}
