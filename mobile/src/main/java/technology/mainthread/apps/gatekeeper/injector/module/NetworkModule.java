package technology.mainthread.apps.gatekeeper.injector.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.data.RemoteConfigKeys;
import technology.mainthread.apps.gatekeeper.data.service.GatekeeperService;
import technology.mainthread.apps.gatekeeper.model.GatekeeperAdapterFactory;

@Module
public class NetworkModule {

    private final Context context;
    private final Resources resources;

    public NetworkModule(Application application) {
        this.context = application.getApplicationContext();
        this.resources = application.getResources();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    Moshi provideMoshi() {
        return new Moshi.Builder()
                .add(GatekeeperAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    GatekeeperService provideGatekeeperService(OkHttpClient okHttpClient, Moshi moshi, FirebaseRemoteConfig config) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(resources.getString(R.string.particle_endpoint, config.getString(RemoteConfigKeys.PARTICLE_DEVICE)))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        return retrofit.create(GatekeeperService.class);
    }

    @Provides
    @Singleton
    Picasso providePicasso(OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }

}
