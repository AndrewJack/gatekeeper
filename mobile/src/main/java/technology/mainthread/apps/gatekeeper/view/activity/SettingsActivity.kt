package technology.mainthread.apps.gatekeeper.view.activity

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.databinding.ActivitySettingsBinding
import technology.mainthread.apps.gatekeeper.view.fragment.buildSettingsFragment

fun getSettingsIntent(context: Context): Intent {
    return Intent(context, SettingsActivity::class.java)
}

class SettingsActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySettingsBinding = DataBindingUtil.setContentView<ActivitySettingsBinding>(this, R.layout.activity_settings)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fm = supportFragmentManager
        if (fm.findFragmentById(R.id.content_main) == null) {
            fm.beginTransaction()
                    .replace(R.id.content_main, buildSettingsFragment())
                    .commit()
        }
    }

}
