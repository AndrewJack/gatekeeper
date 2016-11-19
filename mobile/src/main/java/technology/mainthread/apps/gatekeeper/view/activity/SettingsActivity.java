package technology.mainthread.apps.gatekeeper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.databinding.ActivitySettingsBinding;
import technology.mainthread.apps.gatekeeper.view.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.content_main) == null) {
            fm.beginTransaction()
                    .replace(R.id.content_main, SettingsFragment.newInstance())
                    .commit();
        }
    }

}
