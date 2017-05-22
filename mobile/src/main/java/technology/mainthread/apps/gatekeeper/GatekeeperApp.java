package technology.mainthread.apps.gatekeeper;

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

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.fabric.sdk.android.Fabric;
import io.reactivex.Completable;
import technology.mainthread.apps.gatekeeper.data.CrashlyticsTree;
import technology.mainthread.apps.gatekeeper.data.RemoteConfigKeys;
import technology.mainthread.apps.gatekeeper.injector.component.DaggerAppComponent;
import technology.mainthread.apps.gatekeeper.injector.module.AndroidServicesModule;
import technology.mainthread.apps.gatekeeper.injector.module.AppModule;
import technology.mainthread.apps.gatekeeper.injector.module.FirebaseModule;
import technology.mainthread.apps.gatekeeper.injector.module.GoogleApiModule;
import technology.mainthread.apps.gatekeeper.injector.module.NetworkModule;
import timber.log.Timber;

import static technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper.applyCompletableSchedulers;

public class GatekeeperApp extends DaggerApplication {

    @Inject
    GoogleApiAvailability googleApiAvailability;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.ENABLE_FABRIC) {
            Fabric.with(this, new Crashlytics(), new Beta());
        }
        Timber.plant(BuildConfig.ENABLE_FABRIC ? new CrashlyticsTree() : new Timber.DebugTree());

        Completable.fromAction(() -> setupFirebase())
                .compose(applyCompletableSchedulers())
                .subscribe();

        checkGooglePlayServices();
    }

    @Override
    protected AndroidInjector<GatekeeperApp> applicationInjector() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .androidServicesModule(new AndroidServicesModule(this))
                .firebaseModule(new FirebaseModule())
                .googleApiModule(new GoogleApiModule(this))
                .networkModule(new NetworkModule(this))
                .create(this);
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
