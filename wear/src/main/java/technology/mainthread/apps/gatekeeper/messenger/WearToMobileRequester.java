package technology.mainthread.apps.gatekeeper.messenger;

import javax.inject.Inject;

import rx.Observable;
import technology.mainthread.apps.gatekeeper.common.SharedValues;

public class WearToMobileRequester {

    private WearableMessenger messenger;

    @Inject
    public WearToMobileRequester(WearableMessenger messenger) {
        this.messenger = messenger;
    }

    public Observable<Boolean> prime() {
        return Observable.defer(() ->
                Observable.just(messenger.sendMessage(SharedValues.PATH_PRIME, new byte[0])));
    }

    public Observable<Boolean> unlock() {
        return Observable.defer(() ->
                Observable.just(messenger.sendMessage(SharedValues.PATH_UNLOCK, new byte[0])));
    }
}
