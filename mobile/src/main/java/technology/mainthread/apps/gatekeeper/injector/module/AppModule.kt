package technology.mainthread.apps.gatekeeper.injector.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import technology.mainthread.apps.gatekeeper.data.AuthManager
import technology.mainthread.apps.gatekeeper.data.GatekeeperAuthManager
import technology.mainthread.apps.gatekeeper.data.RegisterDevices
import technology.mainthread.apps.gatekeeper.data.RxBus
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(application: Application) {

    private val context: Context = application.applicationContext

    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Provides
    @Singleton
    internal fun provideApplicationResources(): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    internal fun provideBus(): RxBus {
        return RxBus()
    }

    @Provides
    internal fun provideAuthManager(@Named("auth") googleApiClient: GoogleApiClient, firebaseAuth: FirebaseAuth, registerDevices: RegisterDevices): AuthManager {
        return GatekeeperAuthManager(googleApiClient, firebaseAuth, registerDevices)
    }

}
