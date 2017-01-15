package technology.mainthread.apps.gatekeeper.view.fragment;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import de.psdev.licensesdialog.LicensesDialog;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.common.rx.RxSchedulerHelper;
import technology.mainthread.apps.gatekeeper.data.AndroidAppInfo;
import technology.mainthread.apps.gatekeeper.data.AuthManager;
import technology.mainthread.apps.gatekeeper.data.VibratorTunes;
import technology.mainthread.apps.gatekeeper.view.activity.AuthActivity;

public class SettingsFragment extends RxPreferenceFragment {

    @Inject
    AndroidAppInfo androidAppInfo;
    @Inject
    Vibrator vibrator;
    @Inject
    AuthManager authManager;

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GatekeeperApp.get(getActivity()).inject(this);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findPreference("pref_sign_out").setOnPreferenceClickListener(preference -> {
            authManager.signOut()
                    .compose(RxSchedulerHelper.applyFlowableSchedulers())
                    .compose(bindToLifecycle())
                    .subscribe(success -> {
                        if (success) {
                            showToast(R.string.sign_out_success);
                            onSignOutSuccess();
                        } else {
                            showToast(R.string.sign_out_failure);
                        }
                    });
            return true;
        });
        findPreference("pref_delete").setOnPreferenceClickListener(preference -> {
            authManager.deleteAccount()
                    .compose(RxSchedulerHelper.applyFlowableSchedulers())
                    .compose(bindToLifecycle())
                    .subscribe(success -> {
                        if (success) {
                            showToast(R.string.delete_account_success);
                            onSignOutSuccess();
                        } else {
                            showToast(R.string.delete_account_failure);
                        }
                    });
            return true;
        });

        findPreference("pref_build").setSummary(String.format("%1$s (%2$s)", androidAppInfo.getVersionName(), androidAppInfo.getVersionCode()));
        findPreference("pref_licenses").setOnPreferenceClickListener(preference -> {
            showLicencesDialog();
            return true;
        });

        findPreference("pref_vibrate_tune").setOnPreferenceChangeListener((preference, newValue) -> {
            long[] vibrateTune = VibratorTunes.VIBRATE_TUNES.get(newValue);
            if (vibrateTune != null && vibrateTune.length > 0) {
                vibrator.vibrate(vibrateTune, -1);
            }

            return true;
        });
    }

    private void showLicencesDialog() {
        if (!isRemoving()) {
            new LicensesDialog.Builder(getActivity()).setNotices(R.raw.notices).setIncludeOwnLicense(true).build().show();
        }
    }

    private void showToast(@StringRes int message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void onSignOutSuccess() {
        getActivity().startActivity(AuthActivity.getIntent(getActivity()));
        getActivity().finish();
    }

}
