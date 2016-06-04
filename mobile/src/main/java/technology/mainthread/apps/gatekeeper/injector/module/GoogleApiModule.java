package technology.mainthread.apps.gatekeeper.injector.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import technology.mainthread.apps.gatekeeper.R;

@Module
public class GoogleApiModule {

    private final Context context;
    private Resources resources;

    public GoogleApiModule(Application application) {
        this.context = application.getApplicationContext();
        this.resources = application.getResources();
    }

    @Provides
    @Singleton
    @Named("auth")
    GoogleApiClient provideGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
}
