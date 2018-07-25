package technology.mainthread.apps.gatekeeper

import androidx.collection.ArrayMap

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.Completable
import technology.mainthread.apps.gatekeeper.rx.applyCompletableSchedulers
import technology.mainthread.apps.gatekeeper.data.CrashTree
import technology.mainthread.apps.gatekeeper.data.*
import technology.mainthread.apps.gatekeeper.injector.component.DaggerAppComponent
import technology.mainthread.apps.gatekeeper.injector.module.AndroidServicesModule
import technology.mainthread.apps.gatekeeper.injector.module.AppModule
import technology.mainthread.apps.gatekeeper.injector.module.FirebaseModule
import technology.mainthread.apps.gatekeeper.injector.module.GoogleApiModule
import technology.mainthread.apps.gatekeeper.injector.module.NetworkModule
import timber.log.Timber


class GatekeeperApp : DaggerApplication() {

    @Inject
    internal lateinit var googleApiAvailability: GoogleApiAvailability

    override fun onCreate() {
        super.onCreate()
        Timber.plant(if (!BuildConfig.DEBUG) CrashTree() else Timber.DebugTree())

        Completable.fromAction { setupFirebase() }
                .compose(applyCompletableSchedulers())
                .subscribe()

        checkGooglePlayServices()
    }

    override fun applicationInjector(): AndroidInjector<GatekeeperApp> {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .androidServicesModule(AndroidServicesModule(this))
                .firebaseModule(FirebaseModule())
                .googleApiModule(GoogleApiModule(this))
                .networkModule(NetworkModule(this))
                .create(this)
    }

    private fun setupFirebase() {
        Timber.d("Setup firebase")
        val instance = FirebaseDatabase.getInstance()
        instance.setPersistenceEnabled(true)
        instance.setLogLevel(if (BuildConfig.DEBUG) Logger.Level.DEBUG else Logger.Level.NONE)

        val config = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        config.setConfigSettings(configSettings)
        config.setDefaults(defaultsConfig)

        config.fetch().addOnCompleteListener { _ ->
            Timber.d("Config fetched")
            config.activateFetched()

            Timber.d("App version %s", config.getString(APP_VERSION))
        }
    }

    private val defaultsConfig: Map<String, Any>
        get() {
            val map = ArrayMap<String, Any>()
            map.put(PARTICLE_AUTH, getString(R.string.particle_auth))
            map.put(PARTICLE_DEVICE, getString(R.string.particle_device))
            map.put(APP_VERSION, "-1")
            return map
        }

    private fun checkGooglePlayServices() {
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (status != ConnectionResult.SUCCESS) {
            googleApiAvailability.showErrorNotification(this, status)
        }
    }
}
