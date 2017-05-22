package technology.mainthread.apps.gatekeeper;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import technology.mainthread.apps.gatekeeper.injector.component.DaggerAppComponent;
import technology.mainthread.apps.gatekeeper.injector.module.AppModule;
import technology.mainthread.apps.gatekeeper.injector.module.WearModule;
import timber.log.Timber;

public class GatekeeperApp extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new ReleaseTree());
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .wearModule(new WearModule(this))
                .create(this);
    }
}
