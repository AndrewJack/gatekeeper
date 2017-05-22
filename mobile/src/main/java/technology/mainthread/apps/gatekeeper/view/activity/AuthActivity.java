package technology.mainthread.apps.gatekeeper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.databinding.ActivityAuthBinding;
import technology.mainthread.apps.gatekeeper.viewModel.AuthActivityViewModel;

public class AuthActivity extends DaggerAppCompatActivity {

    @Inject
    AuthActivityViewModel viewModel;

    public static Intent getIntent(Context context) {
        return new Intent(context, AuthActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAuthBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        binding.setViewModel(viewModel);
        viewModel.initialize(this, binding);
    }

    @Override
    protected void onDestroy() {
        viewModel.terminate();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.handleActivityResult(requestCode, data);
    }
}
