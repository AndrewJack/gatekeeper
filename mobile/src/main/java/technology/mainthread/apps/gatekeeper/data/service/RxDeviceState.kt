package technology.mainthread.apps.gatekeeper.data.service

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import technology.mainthread.apps.gatekeeper.data.PARTICLE_AUTH
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus
import javax.inject.Inject

class RxDeviceState @Inject
constructor(private val gatekeeperService: GatekeeperService, private val config: FirebaseRemoteConfig) {

    fun gatekeeperStateObservable(): Flowable<GatekeeperState> {
        val auth = config.getString(PARTICLE_AUTH)
        return Flowable.zip<DeviceStatus, DeviceStatus, GatekeeperState>(
                gatekeeperService.doorStatusResult(auth),
                gatekeeperService.primedStatusResult(auth),
                BiFunction { doorStatus, primeStatus -> calculateState(doorStatus, primeStatus) }
        )
    }

    fun calculateState(doorStatus: DeviceStatus?, primeStatus: DeviceStatus?): GatekeeperState {
        if (doorStatus != null && primeStatus != null) {
            if (doorStatus.coreInfo.connected && primeStatus.coreInfo.connected) {
                if (doorStatus.result != 0) {
                    return GatekeeperState.DOOR_OPEN
                }

                if (primeStatus.result > 0) {
                    return GatekeeperState.PRIMED
                }

                return GatekeeperState.ONLINE
            } else {
                return GatekeeperState.OFFLINE
            }
        }

        return GatekeeperState.UNKNOWN
    }

}
