package technology.mainthread.apps.gatekeeper.common.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxSchedulerHelper {

    private RxSchedulerHelper() {
    }

    // http://blog.danlew.net/2015/03/02/dont-break-the-chain/
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
