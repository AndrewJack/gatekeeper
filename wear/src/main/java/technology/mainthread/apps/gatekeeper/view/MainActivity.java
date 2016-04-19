package technology.mainthread.apps.gatekeeper.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.GridViewPager;
import android.view.View;

import javax.inject.Inject;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper;
import technology.mainthread.apps.gatekeeper.messenger.WearToMobileRequester;
import timber.log.Timber;

import static technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper.applySchedulers;

public class MainActivity extends WearableActivity implements ActionGridPagerAdapter.ActionClickListener {

    @Inject
    WearToMobileRequester requester;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private View progress;
    private GridViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GatekeeperApp.get(this).inject(this);
        setContentView(R.layout.activity_main);

        pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ActionGridPagerAdapter(this, this));

        progress = findViewById(R.id.progress);
    }

    @Override
    public void onItemClicked(String action) {
        Timber.d("onItemClicked: %s", action);
        processRequest(action);
    }

    private void processRequest(String action) {
        pager.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        if (GatekeeperAction.ACTION_PRIME.equalsIgnoreCase(action)) {
            subscriptions.add(requester.prime()
                    .compose(applySchedulers())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean result) {
                            handleResult(result);
                        }
                    }));
        } else if (GatekeeperAction.ACTION_UNLOCK.equalsIgnoreCase(action)) {
            subscriptions.add(requester.unlock()
                    .compose(RxSchedulerHelper.<Boolean>applySchedulers())
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean result) {
                            handleResult(result);
                        }
                    }));
        }
    }

    private void handleResult(boolean result) {
        progress.setVisibility(View.GONE);
        int animation = result ? ConfirmationActivity.SUCCESS_ANIMATION : ConfirmationActivity.FAILURE_ANIMATION;
        String message = getString(result ? R.string.request_success : R.string.request_failure);

        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animation);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        startActivity(intent);

        finish();
    }

}
