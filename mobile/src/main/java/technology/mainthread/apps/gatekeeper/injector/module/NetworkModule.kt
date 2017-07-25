package technology.mainthread.apps.gatekeeper.injector.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.moshi.Moshi
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.data.PARTICLE_DEVICE
import technology.mainthread.apps.gatekeeper.data.service.GatekeeperService
import javax.inject.Singleton

@Module
class NetworkModule(application: Application) {

    private val context: Context = application.applicationContext
    private val resources: Resources = application.resources

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    internal fun provideGatekeeperService(okHttpClient: OkHttpClient, moshi: Moshi, config: FirebaseRemoteConfig): GatekeeperService {
        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(resources.getString(R.string.particle_endpoint, config.getString(PARTICLE_DEVICE)))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

        return retrofit.create(GatekeeperService::class.java)
    }

    @Provides
    @Singleton
    internal fun providePicasso(okHttpClient: OkHttpClient): Picasso {
        return Picasso.Builder(context)
                .downloader(OkHttp3Downloader(okHttpClient))
                .build()
    }

}
