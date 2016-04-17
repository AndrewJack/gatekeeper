package technology.mainthread.apps.gatekeeper.service.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.data.preferences.GcmPreferences;
import timber.log.Timber;

public class RegistrationIntentService extends IntentService {

    private static final String[] TOPICS = {"global"};

    @Inject
    SharedPreferences sharedPreferences;

    public RegistrationIntentService() {
        super(RegistrationIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GatekeeperApp.get(this).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Timber.i("GCM Registration Token: %s", token);

            // Register
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token); // TODO

            sharedPreferences.edit().putBoolean(GcmPreferences.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Timber.e(e, "Failed to complete token refresh");
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(GcmPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(GcmPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: subscribe with server / unsub & resub
        sharedPreferences.edit().putString(GcmPreferences.GCM_TOKEN, token).apply();
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
