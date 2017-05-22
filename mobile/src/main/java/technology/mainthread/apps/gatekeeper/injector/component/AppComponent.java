package technology.mainthread.apps.gatekeeper.injector.component;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.injector.module.AndroidServicesModule;
import technology.mainthread.apps.gatekeeper.injector.module.AppModule;
import technology.mainthread.apps.gatekeeper.injector.module.FirebaseModule;
import technology.mainthread.apps.gatekeeper.injector.module.GoogleApiModule;
import technology.mainthread.apps.gatekeeper.injector.module.NetworkModule;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        AndroidServicesModule.class,
        GoogleApiModule.class,
        FirebaseModule.class,
        AndroidBindingModule.class,
        AndroidSupportInjectionModule.class,
})
public interface AppComponent extends AndroidInjector<GatekeeperApp> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<GatekeeperApp> {
        public abstract Builder appModule(AppModule appModule);
        public abstract Builder networkModule(NetworkModule networkModule);
        public abstract Builder androidServicesModule(AndroidServicesModule androidServicesModule);
        public abstract Builder googleApiModule(GoogleApiModule googleApiModule);
        public abstract Builder firebaseModule(FirebaseModule firebaseModule);
    }
}
