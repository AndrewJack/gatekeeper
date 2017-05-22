package technology.mainthread.apps.gatekeeper.view.baseHelpers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;

public abstract class DaggerWearableActivity extends WearableActivity implements HasFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    public AndroidInjector<Fragment> fragmentInjector() {
        return this.fragmentInjector;
    }
}
