package technology.mainthread.apps.gatekeeper.injector.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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

}
