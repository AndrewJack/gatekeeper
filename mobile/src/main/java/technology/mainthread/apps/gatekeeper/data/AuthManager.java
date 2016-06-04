package technology.mainthread.apps.gatekeeper.data;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

import rx.Observable;

public interface AuthManager {

    GoogleApiClient getGoogleApiClient();

    Observable<Boolean> authWithFirebase(GoogleSignInAccount account);

    Observable<Boolean> signOut();

    Observable<Boolean> deleteAccount();

}
