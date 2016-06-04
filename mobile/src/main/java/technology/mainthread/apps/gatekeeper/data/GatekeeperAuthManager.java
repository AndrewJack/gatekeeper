package technology.mainthread.apps.gatekeeper.data;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import technology.mainthread.apps.gatekeeper.common.SharedValues;

public class GatekeeperAuthManager implements AuthManager {

    private final GoogleApiClient googleApiClient;
    private final FirebaseAuth firebaseAuth;

    public GatekeeperAuthManager(GoogleApiClient googleApiClient, FirebaseAuth firebaseAuth) {
        this.googleApiClient = googleApiClient;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    public Observable<Boolean> authWithFirebase(GoogleSignInAccount account) {
        return Observable.defer(() -> Observable.just(authWithFirebaseSync(account)));
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

    private boolean authWithFirebaseSync(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Task<AuthResult> task = firebaseAuth.signInWithCredential(credential);
        return task.isComplete() && task.isSuccessful() && task.getResult().getUser() != null;
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
