package technology.mainthread.apps.gatekeeper.injector.component

import dagger.Module
import dagger.android.ContributesAndroidInjector
import technology.mainthread.apps.gatekeeper.service.ErrorService
import technology.mainthread.apps.gatekeeper.service.WearListenerService
import technology.mainthread.apps.gatekeeper.view.MainActivity

@Module
internal abstract class AndroidBindingModule {

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun errorService(): ErrorService

    @ContributesAndroidInjector
    internal abstract fun wearListenerService(): WearListenerService
}
