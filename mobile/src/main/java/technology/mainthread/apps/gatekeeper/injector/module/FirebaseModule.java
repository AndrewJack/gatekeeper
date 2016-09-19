package technology.mainthread.apps.gatekeeper.injector.module;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    public FirebaseModule() {
    }

    @Provides
    FirebaseAuth provideAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    FirebaseDatabase provideDatabaseReference() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    FirebaseRemoteConfig provideRemoteConfig() {
        return FirebaseRemoteConfig.getInstance();
    }

    @Provides
    FirebaseStorage provideFirebaseStorage() {
        return FirebaseStorage.getInstance();
    }

    @Provides
    FirebaseInstanceId provideFirebaseInstanceId() {
        return FirebaseInstanceId.getInstance();
    }
}
