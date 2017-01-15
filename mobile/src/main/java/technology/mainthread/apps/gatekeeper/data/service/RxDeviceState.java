package technology.mainthread.apps.gatekeeper.data.service;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import javax.inject.Inject;

import io.reactivex.Flowable;
import technology.mainthread.apps.gatekeeper.data.RemoteConfigKeys;
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus;

public class RxDeviceState {

    private final GatekeeperService gatekeeperService;
    private final FirebaseRemoteConfig config;

    @Inject
    public RxDeviceState(GatekeeperService gatekeeperService, FirebaseRemoteConfig config) {
        this.gatekeeperService = gatekeeperService;
        this.config = config;
    }

    public Flowable<String> gatekeeperStateObservable() {
        String auth = config.getString(RemoteConfigKeys.PARTICLE_AUTH);
        return Flowable.zip(
                gatekeeperService.doorStatusResult(auth),
                gatekeeperService.primedStatusResult(auth),
                (DeviceStatus doorStatus, DeviceStatus primeStatus) -> {

                    if (doorStatus != null && primeStatus != null) {
                        if (doorStatus.coreInfo().connected() && primeStatus.coreInfo().connected()) {
                            if (doorStatus.result() != 0) {
                                return GatekeeperState.DOOR_OPEN;
                            }

                            if (primeStatus.result() > 0) {
                                return GatekeeperState.PRIMED;
                            }

                            return GatekeeperState.ONLINE;
                        } else {
                            return GatekeeperState.OFFLINE;
                        }
                    }

                    return GatekeeperState.UNKNOWN;
                });
    }

}
