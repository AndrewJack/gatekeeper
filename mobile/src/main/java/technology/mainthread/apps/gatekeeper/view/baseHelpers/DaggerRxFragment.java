package technology.mainthread.apps.gatekeeper.view.baseHelpers;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.trello.rxlifecycle2.components.support.RxFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

public abstract class DaggerRxFragment extends RxFragment implements HasSupportFragmentInjector {

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
