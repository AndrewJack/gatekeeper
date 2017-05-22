package technology.mainthread.apps.gatekeeper.injector.component;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.injector.module.AppModule;
import technology.mainthread.apps.gatekeeper.injector.module.WearModule;

@Singleton
@Component(modules = {
        AppModule.class,
        WearModule.class,
        AndroidBindingModule.class,
        AndroidSupportInjectionModule.class
})
public interface AppComponent extends AndroidInjector<GatekeeperApp> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<GatekeeperApp> {
        public abstract Builder appModule(AppModule appModule);

        public abstract Builder wearModule(WearModule wearModule);
    }
}
