package technology.mainthread.apps.gatekeeper.injector.component;

import javax.inject.Singleton;

import dagger.Component;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.injector.graph.AppGraph;
import technology.mainthread.apps.gatekeeper.injector.module.AppModule;
import technology.mainthread.apps.gatekeeper.injector.module.WearModule;

@Singleton
@Component(modules = {AppModule.class, WearModule.class})
public interface AppComponent extends AppGraph {

    final class Initializer {
        public static AppComponent init(GatekeeperApp app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .wearModule(new WearModule(app))
                    .build();
        }

        private Initializer() {
        } // No instances.
    }

}
