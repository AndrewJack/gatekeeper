package technology.mainthread.apps.gatekeeper.injector.component;

import javax.inject.Singleton;

import dagger.Component;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.injector.graph.AppGraph;
import technology.mainthread.apps.gatekeeper.injector.module.AndroidServicesModule;
import technology.mainthread.apps.gatekeeper.injector.module.AppModule;
import technology.mainthread.apps.gatekeeper.injector.module.NetworkModule;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, AndroidServicesModule.class})
public interface AppComponent extends AppGraph {

    final class Initializer {
        public static AppComponent init(GatekeeperApp app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app.getApplicationContext()))
                    .networkModule(new NetworkModule(app.getResources()))
                    .androidServicesModule(new AndroidServicesModule(app.getApplicationContext()))
                    .build();
        }

        private Initializer() {
        } // No instances.
    }

}
