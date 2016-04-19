package technology.mainthread.apps.gatekeeper.injector.graph;

import technology.mainthread.apps.gatekeeper.service.ErrorService;
import technology.mainthread.apps.gatekeeper.service.WearListenerService;
import technology.mainthread.apps.gatekeeper.view.MainActivity;

public interface AppGraph {

    void inject(ErrorService errorService);

    void inject(WearListenerService wearListenerService);

    void inject(MainActivity mainActivity);

}
