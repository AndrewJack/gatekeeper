package technology.mainthread.apps.gatekeeper.viewModel

import android.content.Intent
import androidx.databinding.BaseObservable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import technology.mainthread.apps.gatekeeper.rx.applyFlowableSchedulers
import technology.mainthread.apps.gatekeeper.data.AuthManager
import technology.mainthread.apps.gatekeeper.databinding.ActivityAuthBinding
import technology.mainthread.apps.gatekeeper.view.activity.FRAG_UNLOCK
import technology.mainthread.apps.gatekeeper.view.activity.getMainIntent
import timber.log.Timber
import javax.inject.Inject

class AuthActivityViewModel @Inject internal constructor(private val authManager: AuthManager) : BaseObservable() {

    private val RC_SIGN_IN = 11

    private var activity: AppCompatActivity? = null
    private var googleApiClient: GoogleApiClient? = null

    fun initialize(activity: AppCompatActivity, binding: ActivityAuthBinding) {
        this.activity = activity
        binding.signInButton.setOnClickListener { _ -> onSignInClicked() }
        binding.signInButton.isEnabled = false

        googleApiClient = authManager.googleApiClient
        googleApiClient!!.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(bundle: Bundle?) {
                binding.signInButton.isEnabled = true
            }

            override fun onConnectionSuspended(i: Int) {
                binding.signInButton.isEnabled = false
            }
        })
        googleApiClient!!.registerConnectionFailedListener { _ -> binding.signInButton.isEnabled = false }
        googleApiClient!!.connect()
    }

    fun terminate() {
        googleApiClient?.disconnect()
        googleApiClient = null
    }

    private fun onSignInClicked() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(authManager.googleApiClient)
        activity?.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleActivityResult(requestCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && data != null) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            Timber.d("Google signin result: %s", result.isSuccess)
            if (result.isSuccess) {
                authManager.authWithFirebase(result.signInAccount!!)
                        .compose(applyFlowableSchedulers<Boolean>())
                        .subscribe { _ ->
                            Toast.makeText(activity, "Signed in", Toast.LENGTH_SHORT).show()
                            activity!!.startActivity(getMainIntent(activity!!, FRAG_UNLOCK))
                            activity!!.finish()
                        }
            }
            // TODO: display error
        }
    }

}
