package technology.mainthread.apps.gatekeeper.messenger;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import technology.mainthread.apps.gatekeeper.common.SharedValues;

public class WearToMobileRequester {

    private WearableMessenger messenger;

    @Inject
    public WearToMobileRequester(WearableMessenger messenger) {
        this.messenger = messenger;
    }

    public Flowable<Boolean> prime() {
        FlowableOnSubscribe<Boolean> source = e -> {
            e.onNext(messenger.sendMessage(SharedValues.PATH_PRIME, new byte[0]));
            e.onComplete();
        };
        return Flowable.create(source, BackpressureStrategy.BUFFER);
    }

    public Flowable<Boolean> unlock() {
        FlowableOnSubscribe<Boolean> source = e -> {
            e.onNext(messenger.sendMessage(SharedValues.PATH_UNLOCK, new byte[0]));
            e.onComplete();
        };
        return Flowable.create(source, BackpressureStrategy.BUFFER);
    }
}
