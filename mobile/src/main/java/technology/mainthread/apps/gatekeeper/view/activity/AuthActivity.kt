package technology.mainthread.apps.gatekeeper.view.activity

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.databinding.ActivityAuthBinding
import technology.mainthread.apps.gatekeeper.viewModel.AuthActivityViewModel
import javax.inject.Inject

fun getAuthIntent(context: Context): Intent {
    return Intent(context, AuthActivity::class.java)
}

class AuthActivity : DaggerAppCompatActivity() {

    @Inject
    internal lateinit var viewModel: AuthActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAuthBinding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        binding.viewModel = viewModel
        viewModel.initialize(this, binding)
    }

    override fun onDestroy() {
        viewModel.terminate()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleActivityResult(requestCode, data)
    }

}
