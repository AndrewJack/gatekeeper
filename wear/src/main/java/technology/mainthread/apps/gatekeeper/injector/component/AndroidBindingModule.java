package technology.mainthread.apps.gatekeeper.injector.component;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import technology.mainthread.apps.gatekeeper.service.ErrorService;
import technology.mainthread.apps.gatekeeper.service.WearListenerService;
import technology.mainthread.apps.gatekeeper.view.MainActivity;

@Module
abstract class AndroidBindingModule {

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract ErrorService errorService();

    @ContributesAndroidInjector
    abstract WearListenerService wearListenerService();
}
