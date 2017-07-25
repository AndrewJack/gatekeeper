package technology.mainthread.apps.gatekeeper.injector.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(application: Application) {

    private val context: Context = application.applicationContext

    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Provides
    @Singleton
    internal fun provideApplicationResources(): Resources {
        return context.resources
    }

}
