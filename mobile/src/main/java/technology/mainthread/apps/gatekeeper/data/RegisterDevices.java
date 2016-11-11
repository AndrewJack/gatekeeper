package technology.mainthread.apps.gatekeeper.data;

import android.os.Build;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.model.firebase.Device;

public class RegisterDevices {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseDatabase firebaseDatabase;
    private final FirebaseInstanceId firebaseInstanceId;

    @Inject
    public RegisterDevices(FirebaseAuth firebaseAuth,
                           FirebaseDatabase firebaseDatabase,
                           FirebaseInstanceId firebaseInstanceId) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseInstanceId = firebaseInstanceId;
    }

    public void registerDevice() {
        String token = firebaseInstanceId.getToken();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (token != null && user != null) {
            Device device = Device.builder()
                    .userId(user.getUid())
                    .deviceName(Build.MANUFACTURER + Build.PRODUCT)
                    .pushToken(token)
                    .build();
            firebaseDatabase.getReference().child("devices").setValue(device);
        }
    }
}
