package technology.mainthread.apps.gatekeeper.injector.graph;

import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.service.MobileWearListenerService;
import technology.mainthread.apps.gatekeeper.service.gcm.GatekeeperGcmListenerService;
import technology.mainthread.apps.gatekeeper.service.gcm.RegistrationIntentService;
import technology.mainthread.apps.gatekeeper.service.GatekeeperIntentService;
import technology.mainthread.apps.gatekeeper.view.widget.PrimeWidgetService;
import technology.mainthread.apps.gatekeeper.view.fragment.LogsFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.SettingsFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.UnlockFragment;

public interface AppGraph {
    void inject(GatekeeperApp gatekeeperApp);

    void inject(UnlockFragment unlockFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(GatekeeperGcmListenerService gatekeeperGcmListenerService);

    void inject(RegistrationIntentService registrationIntentService);

    void inject(GatekeeperIntentService gatekeeperIntentService);

    void inject(LogsFragment logsFragment);

    void inject(PrimeWidgetService primeWidgetService);

    void inject(MobileWearListenerService mobileWearListenerService);
}
