package technology.mainthread.apps.gatekeeper.data;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import technology.mainthread.apps.gatekeeper.common.SharedValues;
import timber.log.Timber;

public class GatekeeperAuthManager implements AuthManager {

    private static final int RC_SIGN_IN = 11;

    private final GoogleApiClient googleApiClient;
    private final FirebaseAuth firebaseAuth;

    public GatekeeperAuthManager(GoogleApiClient googleApiClient, FirebaseAuth firebaseAuth) {
        this.googleApiClient = googleApiClient;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void signIn(Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void handleSignInResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Timber.d("Google signin result: %s", result.isSuccess());
            if (result.isSuccess()) {
                authenticateWithFirebase(result.getSignInAccount());
            } else {
                // TODO: display error
            }
        }
    }

    @Override
    public Observable<Boolean> signOut() {
        return Observable.defer(() -> Observable.just(signOutSync()));
    }

    @Override
    public Observable<Boolean> deleteAccount() {
        return Observable.defer(() -> Observable.just(deleteAccountSync()));
    }

    private ConnectionResult connect() {
        return googleApiClient.blockingConnect(SharedValues.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
    }

    private void authenticateWithFirebase(GoogleSignInAccount acct) {
        FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
            Timber.d("auth listener");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Timber.d("onAuthStateChanged:signed_in: %s", user.getUid());
            } else {
                Timber.d("onAuthStateChanged:signed_out");
            }
        };


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).isSuccessful();
    }

    private boolean signOutSync() {
        firebaseAuth.signOut();
        boolean result = connect().isSuccess() && Auth.GoogleSignInApi.signOut(googleApiClient).await().isSuccess();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        return result;
    }

    private boolean deleteAccountSync() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // TODO: need to handle re-authenticating - https://firebase.google.com/docs/auth/android/manage-users#delete_a_user
        boolean result = user.delete().isSuccessful() && connect().isSuccess() && Auth.GoogleSignInApi.revokeAccess(googleApiClient).await().isSuccess();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        return result;
    }
}
