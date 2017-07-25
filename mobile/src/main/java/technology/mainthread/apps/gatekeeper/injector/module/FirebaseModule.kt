package technology.mainthread.apps.gatekeeper.injector.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage

import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    internal fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    internal fun provideDatabaseReference(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    internal fun provideRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance()
    }

    @Provides
    internal fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    internal fun provideFirebaseInstanceId(): FirebaseInstanceId {
        return FirebaseInstanceId.getInstance()
    }

    @Provides
    internal fun providFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}
