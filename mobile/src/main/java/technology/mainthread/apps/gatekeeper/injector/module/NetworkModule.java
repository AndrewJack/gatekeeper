package technology.mainthread.apps.gatekeeper.injector.module;

import android.content.res.Resources;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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
import technology.mainthread.apps.gatekeeper.data.service.GatekeeperService;
import technology.mainthread.apps.gatekeeper.model.particle.CoreInfo;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceAction;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus;
import technology.mainthread.apps.gatekeeper.util.StethoUtil;
import timber.log.Timber;

@Module
public class NetworkModule {

    private final Resources resources;

    public NetworkModule(Resources resources) {
        this.resources = resources;
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
    GatekeeperService provideGatekeeperService(OkHttpClient okHttpClient, Moshi moshi) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(resources.getString(R.string.particle_endpoint, resources.getString(R.string.particle_device)))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        return retrofit.create(GatekeeperService.class);
    }

    @Provides
    @Singleton
    Firebase providesFirebase() {
        Firebase firebase = new Firebase(resources.getString(R.string.firebase_endpoint));
        String token = resources.getString(R.string.firebase_secret);
        if (!token.isEmpty()) {
            firebase.authWithCustomToken(token, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticationError(FirebaseError error) {
                    Timber.w("Login Failed! %s", error.getMessage());
                }

                @Override
                public void onAuthenticated(AuthData authData) {
                    Timber.d("Login Succeeded!");
                }
            });
        }
        return firebase;
    }

}
