package technology.mainthread.apps.gatekeeper;

import android.app.Application;
import android.content.Context;

import technology.mainthread.apps.gatekeeper.injector.component.AppComponent;
import timber.log.Timber;

public class GatekeeperApp extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = AppComponent.Initializer.init(this);

        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new ReleaseTree());
    }

    public static AppComponent get(Context context) {
        return ((GatekeeperApp) context.getApplicationContext()).component;
    }

}
