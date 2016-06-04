package technology.mainthread.apps.gatekeeper.viewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper;
import technology.mainthread.apps.gatekeeper.data.AuthManager;
import technology.mainthread.apps.gatekeeper.databinding.ActivityAuthBinding;
import technology.mainthread.apps.gatekeeper.view.activity.MainActivity;
import timber.log.Timber;

public class AuthActivityViewModel extends BaseObservable {

    private static final int RC_SIGN_IN = 11;

    private final AuthManager authManager;

    private AppCompatActivity activity;
    private GoogleApiClient googleApiClient;

    @Inject
    AuthActivityViewModel(AuthManager authManager) {
        this.authManager = authManager;
    }

    public void initialize(AppCompatActivity activity, ActivityAuthBinding binding) {
        this.activity = activity;
        binding.signInButton.setOnClickListener(v -> onSignInClicked());
        binding.signInButton.setEnabled(false);

        googleApiClient = authManager.getGoogleApiClient();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                binding.signInButton.setEnabled(true);
            }

            @Override
            public void onConnectionSuspended(int i) {
                binding.signInButton.setEnabled(false);
            }
        });
        googleApiClient.registerConnectionFailedListener(connectionResult -> binding.signInButton.setEnabled(false));
        googleApiClient.connect();
    }

    public void terminate() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    private void onSignInClicked() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(authManager.getGoogleApiClient());
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleActivityResult(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Timber.d("Google signin result: %s", result.isSuccess());
            if (result.isSuccess()) {
                authManager.authWithFirebase(result.getSignInAccount())
                        .compose(RxSchedulerHelper.applySchedulers())
                        .subscribe(success -> {
                            Toast.makeText(activity, "Signed in", Toast.LENGTH_SHORT).show();
                            activity.startActivity(MainActivity.getMainIntent(activity, MainActivity.FRAG_UNLOCK));
                            activity.finish();
                        });
            }
            // TODO: display error
        }
    }

}
