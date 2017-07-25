package technology.mainthread.apps.gatekeeper.injector.module

import android.app.Application
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import technology.mainthread.apps.gatekeeper.injector.WearAppClient
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
class WearModule(application: Application) {

    private val context: Context = application.applicationContext

    @Provides
    @Singleton
    @WearAppClient
    internal fun providesWearApiClient(): GoogleApiClient {
        return GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build()
    }
}
