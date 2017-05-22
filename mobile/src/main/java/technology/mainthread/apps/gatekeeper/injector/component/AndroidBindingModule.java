package technology.mainthread.apps.gatekeeper.injector.component;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import technology.mainthread.apps.gatekeeper.service.GatekeeperStateService;
import technology.mainthread.apps.gatekeeper.service.MessagingInstanceIdService;
import technology.mainthread.apps.gatekeeper.service.MessagingService;
import technology.mainthread.apps.gatekeeper.service.MobileWearListenerService;
import technology.mainthread.apps.gatekeeper.service.RefreshFCMSubscriptionsService;
import technology.mainthread.apps.gatekeeper.view.activity.AuthActivity;
import technology.mainthread.apps.gatekeeper.view.activity.MainActivity;
import technology.mainthread.apps.gatekeeper.view.activity.SettingsActivity;
import technology.mainthread.apps.gatekeeper.view.fragment.ImagesFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.LogsFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.SettingsFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.UnlockFragment;

@Module
abstract class AndroidBindingModule {

    // Activity
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract AuthActivity authActivity();

    @ContributesAndroidInjector
    abstract SettingsActivity settingsActivity();

    // Fragment
    @ContributesAndroidInjector
    abstract ImagesFragment imagesFragment();

    @ContributesAndroidInjector
    abstract LogsFragment logsFragment();

    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();

    @ContributesAndroidInjector
    abstract UnlockFragment unlockFragment();

    // Service
    @ContributesAndroidInjector
    abstract GatekeeperStateService gatekeeperStateService();

    @ContributesAndroidInjector
    abstract MessagingInstanceIdService messagingInstanceIdService();

    @ContributesAndroidInjector
    abstract MessagingService messagingService();

    @ContributesAndroidInjector
    abstract MobileWearListenerService mobileWearListenerService();

    @ContributesAndroidInjector
    abstract RefreshFCMSubscriptionsService refreshFCMSubscriptionsService();
}
