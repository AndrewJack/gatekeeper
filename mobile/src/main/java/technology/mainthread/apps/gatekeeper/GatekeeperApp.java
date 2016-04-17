package technology.mainthread.apps.gatekeeper;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Config;
import com.firebase.client.Firebase;
import com.firebase.client.Logger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import rx.Observable;
import technology.mainthread.apps.gatekeeper.data.CrashlyticsTree;
import technology.mainthread.apps.gatekeeper.data.preferences.GcmPreferences;
import technology.mainthread.apps.gatekeeper.injector.component.AppComponent;
import technology.mainthread.apps.gatekeeper.service.gcm.RegistrationIntentService;
import technology.mainthread.apps.gatekeeper.util.StethoUtil;
import timber.log.Timber;

import static technology.mainthread.apps.gatekeeper.util.RxSchedulerUtil.applySchedulers;

public class GatekeeperApp extends Application {

    @Inject
    GoogleApiAvailability googleApiAvailability;
    @Inject
    SharedPreferences sharedPreferences;

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        StethoUtil.setUpStetho(this);
        component = AppComponent.Initializer.init(this);
        component.inject(this);

        if (BuildConfig.ENABLE_FABRIC) {
            Fabric.with(this, new Crashlytics());
        }
        Timber.plant(BuildConfig.ENABLE_FABRIC ? new CrashlyticsTree() : new Timber.DebugTree());

        Observable.defer(() -> Observable.just(setupFirebase()))
                .compose(applySchedulers())
                .subscribe();

        checkGooglePlayServices();

        if (sharedPreferences.getString(GcmPreferences.GCM_TOKEN, null) == null) {
            startService(new Intent(this, RegistrationIntentService.class));
        }
    }

    private Void setupFirebase() {
        Config config = new Config();
        config.setPersistenceEnabled(true);
        config.setLogLevel(BuildConfig.DEBUG ? Logger.Level.DEBUG : Logger.Level.NONE);
        Firebase.setDefaultConfig(config);

        Firebase.setAndroidContext(getApplicationContext());
        return null;
    }

    private void checkGooglePlayServices() {
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            googleApiAvailability.showErrorNotification(this, status);
        }
    }

    public static AppComponent get(Context context) {
        return ((GatekeeperApp) context.getApplicationContext()).component;
    }
}
