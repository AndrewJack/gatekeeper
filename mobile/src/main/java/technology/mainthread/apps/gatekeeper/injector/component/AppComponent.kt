package technology.mainthread.apps.gatekeeper.injector.component

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import technology.mainthread.apps.gatekeeper.GatekeeperApp
import technology.mainthread.apps.gatekeeper.injector.module.*
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, AndroidServicesModule::class, GoogleApiModule::class, FirebaseModule::class, AndroidBindingModule::class, AndroidSupportInjectionModule::class))
interface AppComponent : AndroidInjector<GatekeeperApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<GatekeeperApp>() {
        abstract fun appModule(appModule: AppModule): Builder
        abstract fun networkModule(networkModule: NetworkModule): Builder
        abstract fun androidServicesModule(androidServicesModule: AndroidServicesModule): Builder
        abstract fun googleApiModule(googleApiModule: GoogleApiModule): Builder
        abstract fun firebaseModule(firebaseModule: FirebaseModule): Builder
    }
}
