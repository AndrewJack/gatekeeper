package technology.mainthread.apps.gatekeeper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.beta.Beta;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Map;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import io.reactivex.Completable;
import technology.mainthread.apps.gatekeeper.data.CrashlyticsTree;
import technology.mainthread.apps.gatekeeper.data.RemoteConfigKeys;
import technology.mainthread.apps.gatekeeper.injector.component.AppComponent;
import technology.mainthread.apps.gatekeeper.util.StethoUtil;
import timber.log.Timber;

import static technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper.applyCompletableSchedulers;

public class GatekeeperApp extends Application {

    @Inject
    GoogleApiAvailability googleApiAvailability;
    @Inject
    SharedPreferences sharedPreferences;

    private AppComponent component;

    public static AppComponent get(Context context) {
        return ((GatekeeperApp) context.getApplicationContext()).component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        StethoUtil.setUpStetho(this);
        component = AppComponent.Initializer.init(this);
        component.inject(this);

        if (BuildConfig.ENABLE_FABRIC) {
            Fabric.with(this, new Crashlytics(), new Beta());
        }
        Timber.plant(BuildConfig.ENABLE_FABRIC ? new CrashlyticsTree() : new Timber.DebugTree());

        Completable.fromAction(() -> setupFirebase())
                .compose(applyCompletableSchedulers())
                .subscribe();

        checkGooglePlayServices();
    }

    private void setupFirebase() {
        Timber.d("Setup firebase");
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        instance.setPersistenceEnabled(true);
        instance.setLogLevel(BuildConfig.DEBUG ? Logger.Level.DEBUG : Logger.Level.NONE);

        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        config.setConfigSettings(configSettings);
        config.setDefaults(getDefaultsConfig());

        config.fetch().addOnCompleteListener(task -> {
            Timber.d("Config fetched");
            config.activateFetched();

            Timber.d("App version %s", config.getString(RemoteConfigKeys.APP_VERSION));
        });
    }

    private Map<String, Object> getDefaultsConfig() {
        Map<String, Object> map = new ArrayMap<>();
        map.put(RemoteConfigKeys.PARTICLE_AUTH, getString(R.string.particle_auth));
        map.put(RemoteConfigKeys.PARTICLE_DEVICE, getString(R.string.particle_device));
        map.put(RemoteConfigKeys.APP_VERSION, "-1");
        return map;
    }

    private void checkGooglePlayServices() {
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            googleApiAvailability.showErrorNotification(this, status);
        }
    }
}
