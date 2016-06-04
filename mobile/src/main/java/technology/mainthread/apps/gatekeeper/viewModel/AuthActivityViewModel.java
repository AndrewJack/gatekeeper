package technology.mainthread.apps.gatekeeper.viewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.data.AuthManager;
import technology.mainthread.apps.gatekeeper.databinding.ActivityAuthBinding;

public class AuthActivityViewModel extends BaseObservable {

    private final AuthManager authManager;

    private AppCompatActivity activity;

    @Inject
    public AuthActivityViewModel(AuthManager authManager) {
        this.authManager = authManager;
    }

    public void initialize(AppCompatActivity activity, ActivityAuthBinding binding) {
        this.activity = activity;
        binding.signInButton.setOnClickListener(v -> onSignInClicked());
    }

    private void onSignInClicked() {
        if (activity != null && !activity.isFinishing()) {
            authManager.signIn(activity);
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        authManager.handleSignInResult(requestCode, resultCode, data);
    }

}
