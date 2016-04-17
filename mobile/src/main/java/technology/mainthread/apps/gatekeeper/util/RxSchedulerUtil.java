package technology.mainthread.apps.gatekeeper.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxSchedulerUtil {

    private RxSchedulerUtil() {
    }

    // http://blog.danlew.net/2015/03/02/dont-break-the-chain/
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
