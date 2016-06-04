package technology.mainthread.apps.gatekeeper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.databinding.ActivityMainBinding;
import technology.mainthread.apps.gatekeeper.view.fragment.LogsFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.SettingsFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.UnlockFragment;
import technology.mainthread.apps.gatekeeper.viewModel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_FRAGMENT = "extra_fragment";
    public static String FRAG_UNLOCK = "unlock";
    public static String FRAG_LOGS = "logs";
    public static String FRAG_SETTINGS = "settings";

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle;

    public static Intent getMainIntent(Context context, String fragment) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_FRAGMENT, fragment);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new MainActivityViewModel());

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, new Bundle());

        setSupportActionBar(binding.toolbar);

        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        // Authenticate if not signed in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(AuthActivity.getIntent(this));
            finish();
            return;
        }

        if (getSupportFragmentManager().findFragmentById(R.id.content_main) == null) {
            String fragment = getIntent().getStringExtra(EXTRA_FRAGMENT);

            if (FRAG_LOGS.equals(fragment)) {
                binding.navView.setCheckedItem(R.id.nav_logs);
                showFragment(LogsFragment.newInstance());

            } else if (FRAG_SETTINGS.equals(fragment)) {
                binding.navView.setCheckedItem(R.id.nav_settings);
                showFragment(SettingsFragment.newInstance());

            } else {
                binding.navView.setCheckedItem(R.id.nav_unlock);
                showFragment(UnlockFragment.newInstance());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.drawerLayout.removeDrawerListener(toggle);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_unlock) {
            showFragment(UnlockFragment.newInstance());
        } else if (id == R.id.nav_logs) {
            showFragment(LogsFragment.newInstance());
        } else if (id == R.id.nav_settings) {
            showFragment(SettingsFragment.newInstance());
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
    }
}
