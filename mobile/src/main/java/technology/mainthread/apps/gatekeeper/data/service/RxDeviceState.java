package technology.mainthread.apps.gatekeeper.data.service;

import javax.inject.Inject;

import rx.Observable;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus;
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState;

public class RxDeviceState {

    private final GatekeeperService gatekeeperService;

    @Inject
    public RxDeviceState(GatekeeperService gatekeeperService) {
        this.gatekeeperService = gatekeeperService;
    }

    public Observable<String> gatekeeperStateObservable() {
        return Observable.zip(
                gatekeeperService.doorStatusResult(),
                gatekeeperService.primedStatusResult(),
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
