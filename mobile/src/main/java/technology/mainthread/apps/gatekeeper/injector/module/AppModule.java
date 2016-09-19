package technology.mainthread.apps.gatekeeper.injector.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import technology.mainthread.apps.gatekeeper.data.AuthManager;
import technology.mainthread.apps.gatekeeper.data.GatekeeperAuthManager;
import technology.mainthread.apps.gatekeeper.data.RegisterDevices;
import technology.mainthread.apps.gatekeeper.data.RxBus;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Application application) {
        this.context = application.getApplicationContext();
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return context.getApplicationContext();
    }

    @Provides
    @Singleton
    Resources provideApplicationResources() {
        return context.getResources();
    }

    @Provides
    @Singleton
    RxBus provideBus() {
        return new RxBus();
    }

    @Provides
    AuthManager provideAuthManager(@Named("auth") GoogleApiClient googleApiClient, FirebaseAuth firebaseAuth, RegisterDevices registerDevices) {
        return new GatekeeperAuthManager(googleApiClient, firebaseAuth, registerDevices);
    }

}
