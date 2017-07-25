package technology.mainthread.apps.gatekeeper.injector.module

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Vibrator
import android.preference.PreferenceManager
import android.support.v4.app.NotificationManagerCompat

import com.google.android.gms.common.GoogleApiAvailability

import dagger.Module
import dagger.Provides

@Module
class AndroidServicesModule(application: Application) {

    private val context: Context = application.applicationContext

    @Provides
    internal fun provideNotificationManager(): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }

    @Provides
    internal fun providesGoogleApiAvailability(): GoogleApiAvailability {
        return GoogleApiAvailability.getInstance()
    }

    @Provides
    internal fun providesClipboardManager(): ClipboardManager {
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Provides
    internal fun providesDefaultSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    internal fun providesVibrator(): Vibrator {
        return context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @Provides
    internal fun provideAppWidgetManager(): AppWidgetManager {
        return AppWidgetManager.getInstance(context)
    }

}
