package technology.mainthread.apps.gatekeeper.injector.component

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import technology.mainthread.apps.gatekeeper.GatekeeperApp
import technology.mainthread.apps.gatekeeper.injector.module.AppModule
import technology.mainthread.apps.gatekeeper.injector.module.WearModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, WearModule::class, AndroidBindingModule::class, AndroidSupportInjectionModule::class))
interface AppComponent : AndroidInjector<GatekeeperApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<GatekeeperApp>() {
        abstract fun appModule(appModule: AppModule): Builder

        abstract fun wearModule(wearModule: WearModule): Builder
    }
}
