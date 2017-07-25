package technology.mainthread.apps.gatekeeper

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import technology.mainthread.apps.gatekeeper.injector.component.DaggerAppComponent
import technology.mainthread.apps.gatekeeper.injector.module.AppModule
import technology.mainthread.apps.gatekeeper.injector.module.WearModule
import timber.log.Timber

class GatekeeperApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else ReleaseTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .wearModule(WearModule(this))
                .create(this)
    }
}
