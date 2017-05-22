package technology.mainthread.apps.gatekeeper.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.GridViewPager;
import android.view.View;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper;
import technology.mainthread.apps.gatekeeper.messenger.WearToMobileRequester;
import technology.mainthread.apps.gatekeeper.view.baseHelpers.DaggerWearableActivity;

import static technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper.applyFlowableSchedulers;

public class MainActivity extends DaggerWearableActivity implements ActionGridPagerAdapter.ActionClickListener {

    @Inject
    WearToMobileRequester requester;

    private CompositeDisposable cs = new CompositeDisposable();
    private View progress;
    private GridViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ActionGridPagerAdapter(this, this));

        progress = findViewById(R.id.progress);
    }

    @Override
    protected void onDestroy() {
        cs.clear();
        super.onDestroy();
    }

    @Override
    public void onItemClicked(String action) {
        processRequest(action);
    }

    private void processRequest(String action) {
        pager.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        if (GatekeeperAction.ACTION_PRIME.equalsIgnoreCase(action)) {
            cs.add(requester.prime()
                    .compose(applyFlowableSchedulers())
                    .subscribe(this::handleResult));
        } else if (GatekeeperAction.ACTION_UNLOCK.equalsIgnoreCase(action)) {
            cs.add(requester.unlock()
                    .compose(RxSchedulerHelper.<Boolean>applyFlowableSchedulers())
                    .subscribe(this::handleResult));
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
