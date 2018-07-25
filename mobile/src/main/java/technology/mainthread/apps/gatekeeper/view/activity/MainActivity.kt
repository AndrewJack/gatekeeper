package technology.mainthread.apps.gatekeeper.view.activity

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerAppCompatActivity
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.databinding.ActivityMainBinding
import technology.mainthread.apps.gatekeeper.view.fragment.buildComingSoonFragment
import technology.mainthread.apps.gatekeeper.view.fragment.buildLogsFragment
import technology.mainthread.apps.gatekeeper.view.fragment.buildUnlockFragment
import technology.mainthread.apps.gatekeeper.viewModel.MainActivityViewModel
import javax.inject.Inject

const private val EXTRA_FRAGMENT = "extra_fragment"
const val FRAG_UNLOCK = "unlock"
const val FRAG_LOGS = "logs"
const val FRAG_CAMERA = "camera"

fun getMainIntent(context: Context, fragment: String): Intent {
    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra(EXTRA_FRAGMENT, fragment)
    return intent
}

class MainActivity : DaggerAppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    @Inject
    internal lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = MainActivityViewModel()

        val analytics = FirebaseAnalytics.getInstance(this)
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, Bundle())

        setSupportActionBar(binding.toolbar)

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)

        // Authenticate if not signed in
        if (firebaseAuth.currentUser == null) {
            startActivity(getAuthIntent(this))
            finish()
            return
        }

        showInitialFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        handleOnItemSelected(item)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        handleOnItemSelected(item)
        return true
    }

    private fun showInitialFragment() {
        if (supportFragmentManager.findFragmentById(R.id.content_main) == null) {
            var fragment: String? = intent.getStringExtra(EXTRA_FRAGMENT)
            fragment = if (fragment == null) "" else fragment
            when (fragment) {
                FRAG_LOGS -> showFragment(buildLogsFragment())
                FRAG_CAMERA -> showFragment(buildComingSoonFragment())
                else -> showFragment(buildUnlockFragment())
            }
        }
    }

    private fun handleOnItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_unlock -> showFragment(buildUnlockFragment())
            R.id.nav_logs -> showFragment(buildLogsFragment())
            R.id.nav_camera -> showFragment(buildComingSoonFragment())
            R.id.nav_settings -> startActivity(getSettingsIntent(this))
            else -> {
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()
    }

}
