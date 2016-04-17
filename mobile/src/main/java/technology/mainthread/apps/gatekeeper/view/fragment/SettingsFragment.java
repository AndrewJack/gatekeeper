package technology.mainthread.apps.gatekeeper.view.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import de.psdev.licensesdialog.LicensesDialog;
import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.data.AndroidAppInfo;
import technology.mainthread.apps.gatekeeper.data.VibratorTunes;
import technology.mainthread.apps.gatekeeper.data.preferences.GcmPreferences;
import timber.log.Timber;

public class SettingsFragment extends RxPreferenceFragment {

    @Inject
    ClipboardManager clipboard;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    AndroidAppInfo androidAppInfo;
    @Inject
    Vibrator vibrator;

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
        findPreference("pref_gcm_token")
                .setOnPreferenceClickListener(preference -> {
                    String token = sharedPreferences.getString(GcmPreferences.GCM_TOKEN, null);
                    ClipData clip = ClipData.newPlainText("gcm_token", token);
                    clipboard.setPrimaryClip(clip);
                    Timber.d("Copied token: %s", token);
                    showToast(String.format("Copied token: %s", token));
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

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
