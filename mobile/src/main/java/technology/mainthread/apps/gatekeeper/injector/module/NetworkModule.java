package technology.mainthread.apps.gatekeeper.injector.module;

import android.app.Application;
import android.content.res.Resources;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import technology.mainthread.apps.gatekeeper.BuildConfig;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.data.RemoteConfigKeys;
import technology.mainthread.apps.gatekeeper.data.service.GatekeeperService;
import technology.mainthread.apps.gatekeeper.model.particle.CoreInfo;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceAction;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus;
import technology.mainthread.apps.gatekeeper.util.StethoUtil;

@Module
public class NetworkModule {

    private final Resources resources;

    public NetworkModule(Application application) {
        this.resources = application.getResources();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor());
            StethoUtil.addInterceptor(builder);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    Moshi provideMoshi() {
        return new Moshi.Builder()
                .add(DeviceAction.typeAdapter())
                .add(DeviceStatus.typeAdapter())
                .add(CoreInfo.typeAdapter())
                .build();
    }

    @Provides
    @Singleton
    GatekeeperService provideGatekeeperService(OkHttpClient okHttpClient, Moshi moshi, FirebaseRemoteConfig config) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(resources.getString(R.string.particle_endpoint, config.getString(RemoteConfigKeys.PARTICLE_DEVICE)))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        return retrofit.create(GatekeeperService.class);
    }

}
