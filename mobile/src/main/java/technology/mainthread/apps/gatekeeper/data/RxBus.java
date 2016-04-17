package technology.mainthread.apps.gatekeeper.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Singleton
public class RxBus {

    private final Subject<Object, Object> bus;

    @Inject
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
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
