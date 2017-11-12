package technology.mainthread.apps.gatekeeper.view.baseHelpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

public abstract class DaggerPreferenceFragment extends PreferenceFragmentCompat implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return this.childFragmentInjector;
    }

}
