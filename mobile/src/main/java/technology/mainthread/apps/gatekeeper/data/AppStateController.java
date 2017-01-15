package technology.mainthread.apps.gatekeeper.data;

import android.support.annotation.StringRes;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import technology.mainthread.apps.gatekeeper.model.event.AppEvent;
import technology.mainthread.apps.gatekeeper.model.event.AppEventType;
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState;
import timber.log.Timber;

@Singleton
public class AppStateController {

    private final RxBus bus;

    private @GatekeeperState String lastKnownGatekeeperState = GatekeeperState.UNKNOWN;

    @Inject
    public AppStateController(RxBus bus) {
        this.bus = bus;
    }

    public @GatekeeperState String getLastKnownGatekeeperState() {
        return lastKnownGatekeeperState;
    }

    public void updateGatekeeperState(@GatekeeperState String newState) {
        lastKnownGatekeeperState = newState;
    }

    public void onAppEvent(@AppEventType String appState, boolean success, @StringRes int message) {
        Timber.d("onAppStateChanged: appState: %1$s, gatekeeperState: %2$s, success: %3$s, message: %4$s, ",
                appState, lastKnownGatekeeperState, success, message);
        bus.send(AppEvent.builder()
                .appState(appState)
                .gatekeeperState(lastKnownGatekeeperState)
                .success(success)
                .message(message)
                .build());
    }

    public Observable<AppEvent> getObservable() {
        return bus.toObservable()
                .filter(o -> o instanceof AppEvent)
                .map(o -> (AppEvent) o);
    }

}
