package technology.mainthread.apps.gatekeeper.injector.module;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import technology.mainthread.apps.gatekeeper.data.RxBus;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
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

}
