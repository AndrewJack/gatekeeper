package technology.mainthread.apps.gatekeeper.data;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

import io.reactivex.Flowable;

public interface AuthManager {

    GoogleApiClient getGoogleApiClient();

    Flowable<Boolean> authWithFirebase(GoogleSignInAccount account);

    Flowable<Boolean> signOut();

    Flowable<Boolean> deleteAccount();

}
