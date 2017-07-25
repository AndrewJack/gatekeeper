package technology.mainthread.apps.gatekeeper.data

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.GoogleApiClient

import io.reactivex.Flowable

interface AuthManager {

    val googleApiClient: GoogleApiClient

    fun authWithFirebase(account: GoogleSignInAccount): Flowable<Boolean>

    fun signOut(): Flowable<Boolean>

    fun deleteAccount(): Flowable<Boolean>

}
