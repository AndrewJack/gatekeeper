package technology.mainthread.apps.gatekeeper.data

import android.os.Build
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import technology.mainthread.apps.gatekeeper.model.firebase.Device
import javax.inject.Inject

class RegisterDevices @Inject
constructor(private val firebaseAuth: FirebaseAuth,
            private val firebaseDatabase: FirebaseDatabase,
            private val firebaseInstanceId: FirebaseInstanceId) {

    fun registerDevice() {
        val token = firebaseInstanceId.token
        val user = firebaseAuth.currentUser
        if (token != null && user != null) {
            val device = Device(user.uid, Build.MANUFACTURER + Build.PRODUCT, token)
            firebaseDatabase.reference.child("devices").setValue(device)
        }
    }
}
