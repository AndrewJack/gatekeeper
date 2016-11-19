package technology.mainthread.apps.gatekeeper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.GatekeeperApp;
import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.databinding.ActivityMainBinding;
import technology.mainthread.apps.gatekeeper.view.fragment.ComingSoonFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.LogsFragment;
import technology.mainthread.apps.gatekeeper.view.fragment.UnlockFragment;
import technology.mainthread.apps.gatekeeper.viewModel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_FRAGMENT = "extra_fragment";
    public static final String FRAG_UNLOCK = "unlock";
    public static final String FRAG_LOGS = "logs";
    public static final String FRAG_CAMERA = "camera";

    @Inject
    FirebaseAuth firebaseAuth;

    public static Intent getMainIntent(Context context, String fragment) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_FRAGMENT, fragment);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GatekeeperApp.get(this).inject(this);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new MainActivityViewModel());

        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, new Bundle());

        setSupportActionBar(binding.toolbar);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);

        // Authenticate if not signed in
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(AuthActivity.getIntent(this));
            finish();
            return;
        }

        showInitialFragment();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        handleOnItemSelected(item);
        return true;
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        handleOnItemSelected(item);
        return true;
    }

    private void showInitialFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.content_main) == null) {
            String fragment = getIntent().getStringExtra(EXTRA_FRAGMENT);
            fragment = fragment == null ? "" : fragment;
            switch (fragment) {
                case FRAG_LOGS:
                    showFragment(LogsFragment.newInstance());
                    break;
                case FRAG_CAMERA:
                    showFragment(ComingSoonFragment.newInstance());
                    break;
                default:
                    showFragment(UnlockFragment.newInstance());
                    break;
            }
        }
    }

    private void handleOnItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_unlock:
                showFragment(UnlockFragment.newInstance());
                break;
            case R.id.nav_logs:
                showFragment(LogsFragment.newInstance());
                break;
            case R.id.nav_camera:
                showFragment(ComingSoonFragment.newInstance());
                break;
            case R.id.nav_settings:
                startActivity(SettingsActivity.getIntent(this));
                break;
            default:
                break;
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
    }
}
