package technology.mainthread.apps.gatekeeper.data

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.BackpressureStrategy
import io.reactivex.Emitter
import io.reactivex.Flowable
import technology.mainthread.apps.gatekeeper.common.CONNECTION_TIME_OUT_MS
import timber.log.Timber
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class GatekeeperAuthManager(override val googleApiClient: GoogleApiClient,
                            private val firebaseAuth: FirebaseAuth,
                            private val registerDevices: RegisterDevices) : AuthManager {

    override fun authWithFirebase(account: GoogleSignInAccount): Flowable<Boolean> {
        val source = { e: Emitter<Boolean> ->
            e.onNext(authWithFirebaseSync(account))
            e.onComplete()
        }
        return Flowable.create<Boolean>(source, BackpressureStrategy.BUFFER)
    }

    override fun signOut(): Flowable<Boolean> {
        val source = { e: Emitter<Boolean> ->
            e.onNext(signOutSync())
            e.onComplete()
        }
        return Flowable.create<Boolean>(source, BackpressureStrategy.BUFFER)
    }

    override fun deleteAccount(): Flowable<Boolean> {
        val source = { e: Emitter<Boolean> ->
            e.onNext(deleteAccountSync())
            e.onComplete()
        }
        return Flowable.create<Boolean>(source, BackpressureStrategy.BUFFER)
    }

    private fun connect(): ConnectionResult {
        return googleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
    }

    private fun authWithFirebaseSync(acct: GoogleSignInAccount): Boolean {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        val task = firebaseAuth.signInWithCredential(credential)
        try {
            Tasks.await(task)
        } catch (e: ExecutionException) {
            Timber.d(e, "task await ExecutionException")
        } catch (e: InterruptedException) {
            Timber.e(e, "task await InterruptedException")
        }

        val success = task.isComplete && task.isSuccessful && task.result.user != null

        if (success) {
            registerDevices.registerDevice()
        }

        return success
    }

    private fun signOutSync(): Boolean {
        firebaseAuth.signOut()
        val result = connect().isSuccess && Auth.GoogleSignInApi.signOut(googleApiClient).await().isSuccess
        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
        return result
    }

    private fun deleteAccountSync(): Boolean {
        val user = firebaseAuth.currentUser
        // TODO: need to handle re-authenticating - https://firebase.google.com/docs/auth/android/manage-users#delete_a_user
        val result = user!!.delete().isSuccessful && connect().isSuccess && Auth.GoogleSignInApi.revokeAccess(googleApiClient).await().isSuccess
        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
        return result
    }
}
