package technology.mainthread.apps.gatekeeper.injector.module;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import technology.mainthread.apps.gatekeeper.injector.WearAppClient;

@Module
public class WearModule {

    private final Context context;

    public WearModule(Application application) {
        this.context = application.getApplicationContext();
    }

    @Provides
    @Singleton
    @WearAppClient
    GoogleApiClient providesWearApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }
}
