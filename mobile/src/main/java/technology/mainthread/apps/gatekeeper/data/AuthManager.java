package technology.mainthread.apps.gatekeeper.data;

import android.app.Activity;
import android.content.Intent;

import rx.Observable;

public interface AuthManager {

    void signIn(Activity activity);

    void handleSignInResult(int requestCode, int resultCode, Intent data);

    Observable<Boolean> signOut();

    Observable<Boolean> deleteAccount();

}
