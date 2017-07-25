package technology.mainthread.apps.gatekeeper.injector.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Wearable
import dagger.Module
import dagger.Provides
import technology.mainthread.apps.gatekeeper.R
import javax.inject.Named
import javax.inject.Singleton

@Module
class GoogleApiModule(application: Application) {

    private val context: Context = application.applicationContext
    private val resources: Resources = application.resources

    @Provides
    @Singleton
    @Named("auth")
    internal fun provideGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        return GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    @Provides
    @Singleton
    @Named("wear")
    internal fun providesWearApiClient(): GoogleApiClient {
        return GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build()
    }
}
