package technology.mainthread.apps.gatekeeper.data;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import technology.mainthread.apps.gatekeeper.common.SharedValues;
import timber.log.Timber;

public class GatekeeperAuthManager implements AuthManager {

    private final GoogleApiClient googleApiClient;
    private final FirebaseAuth firebaseAuth;
    private final RegisterDevices registerDevices;

    public GatekeeperAuthManager(GoogleApiClient googleApiClient,
                                 FirebaseAuth firebaseAuth,
                                 RegisterDevices registerDevices) {
        this.googleApiClient = googleApiClient;
        this.firebaseAuth = firebaseAuth;
        this.registerDevices = registerDevices;
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    public Flowable<Boolean> authWithFirebase(GoogleSignInAccount account) {
        FlowableOnSubscribe<Boolean> source = e -> {
            e.onNext(authWithFirebaseSync(account));
            e.onComplete();
        };
        return Flowable.create(source, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Boolean> signOut() {
        FlowableOnSubscribe<Boolean> source = e -> {
            e.onNext(signOutSync());
            e.onComplete();
        };
        return Flowable.create(source, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Boolean> deleteAccount() {
        FlowableOnSubscribe<Boolean> source = e -> {
            e.onNext(deleteAccountSync());
            e.onComplete();
        };
        return Flowable.create(source, BackpressureStrategy.BUFFER);
    }

    private ConnectionResult connect() {
        return googleApiClient.blockingConnect(SharedValues.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
    }

    private boolean authWithFirebaseSync(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Task<AuthResult> task = firebaseAuth.signInWithCredential(credential);
        try {
            Tasks.await(task);
        } catch (ExecutionException e) {
            Timber.d(e, "task await ExecutionException");
        } catch (InterruptedException e) {
            Timber.e(e, "task await InterruptedException");
        }

        boolean success = task.isComplete() && task.isSuccessful() && task.getResult().getUser() != null;

        if (success) {
            registerDevices.registerDevice();
        }

        return success;
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
