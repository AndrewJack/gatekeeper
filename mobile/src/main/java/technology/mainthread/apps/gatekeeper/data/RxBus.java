package technology.mainthread.apps.gatekeeper.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class RxBus {

    private final PublishSubject<Object> bus;

    @Inject
    public RxBus() {
        bus = PublishSubject.create();
    }

    public void send(Object event) {
        bus.onNext(event);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }

}
