package technology.mainthread.apps.gatekeeper.view.fragment

import android.os.Bundle
import android.os.Vibrator
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import de.psdev.licensesdialog.LicensesDialog
import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.common.rx.applyFlowableSchedulers
import technology.mainthread.apps.gatekeeper.data.AndroidAppInfo
import technology.mainthread.apps.gatekeeper.data.AuthManager
import technology.mainthread.apps.gatekeeper.data.VibratorTunes
import technology.mainthread.apps.gatekeeper.view.activity.getAuthIntent
import technology.mainthread.apps.gatekeeper.view.baseHelpers.DaggerRxPreferenceFragment
import javax.inject.Inject

fun buildSettingsFragment(): Fragment {
    return SettingsFragment()
}

class SettingsFragment : DaggerRxPreferenceFragment() {

    @Inject
    internal lateinit var androidAppInfo: AndroidAppInfo
    @Inject
    internal lateinit var vibrator: Vibrator
    @Inject
    internal lateinit var authManager: AuthManager

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreference("pref_sign_out").setOnPreferenceClickListener { _ ->
            authManager.signOut()
                    .compose(applyFlowableSchedulers<Boolean>())
                    .compose(bindToLifecycle<Boolean>())
                    .subscribe { success ->
                        if (success) {
                            showToast(R.string.sign_out_success)
                            onSignOutSuccess()
                        } else {
                            showToast(R.string.sign_out_failure)
                        }
                    }
            true
        }
        findPreference("pref_delete").setOnPreferenceClickListener { _ ->
            authManager.deleteAccount()
                    .compose(applyFlowableSchedulers<Boolean>())
                    .compose(bindToLifecycle<Boolean>())
                    .subscribe { success ->
                        if (success) {
                            showToast(R.string.delete_account_success)
                            onSignOutSuccess()
                        } else {
                            showToast(R.string.delete_account_failure)
                        }
                    }
            true
        }

        findPreference("pref_build").summary = String.format("%1\$s (%2\$s)", androidAppInfo.versionName, androidAppInfo.versionCode)
        findPreference("pref_licenses").setOnPreferenceClickListener { _ ->
            showLicencesDialog()
            true
        }

        findPreference("pref_vibrate_tune").setOnPreferenceChangeListener { _, newValue ->
            val vibrateTune = VibratorTunes.VIBRATE_TUNES[newValue]
            if (vibrateTune != null && vibrateTune.isNotEmpty()) {
                vibrator.vibrate(vibrateTune, -1)
            }

            true
        }
    }

    private fun showLicencesDialog() {
        if (!isRemoving) {
            LicensesDialog.Builder(activity).setNotices(R.raw.notices).setIncludeOwnLicense(true).build().show()
        }
    }

    private fun showToast(@StringRes message: Int) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun onSignOutSuccess() {
        activity?.startActivity(getAuthIntent(activity!!))
        activity?.finish()
    }

}
