package technology.mainthread.apps.gatekeeper.injector.component

import dagger.Module
import dagger.android.ContributesAndroidInjector
import technology.mainthread.apps.gatekeeper.service.*
import technology.mainthread.apps.gatekeeper.view.activity.AuthActivity
import technology.mainthread.apps.gatekeeper.view.activity.MainActivity
import technology.mainthread.apps.gatekeeper.view.activity.SettingsActivity
import technology.mainthread.apps.gatekeeper.view.fragment.ImagesFragment
import technology.mainthread.apps.gatekeeper.view.fragment.LogsFragment
import technology.mainthread.apps.gatekeeper.view.fragment.SettingsFragment
import technology.mainthread.apps.gatekeeper.view.fragment.UnlockFragment

@Module
internal abstract class AndroidBindingModule {

    // Activity
    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun authActivity(): AuthActivity

    @ContributesAndroidInjector
    internal abstract fun settingsActivity(): SettingsActivity

    // Fragment
    @ContributesAndroidInjector
    internal abstract fun imagesFragment(): ImagesFragment

    @ContributesAndroidInjector
    internal abstract fun logsFragment(): LogsFragment

    @ContributesAndroidInjector
    internal abstract fun settingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    internal abstract fun unlockFragment(): UnlockFragment

    // Service
    @ContributesAndroidInjector
    internal abstract fun gatekeeperStateService(): GatekeeperStateService

    @ContributesAndroidInjector
    internal abstract fun messagingInstanceIdService(): MessagingInstanceIdService

    @ContributesAndroidInjector
    internal abstract fun messagingService(): MessagingService

    @ContributesAndroidInjector
    internal abstract fun refreshFCMSubscriptionsService(): RefreshFCMSubscriptionsService
}
