package technology.mainthread.apps.gatekeeper.common.rx;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulerHelper {

    private RxSchedulerHelper() {
    }

    // http://blog.danlew.net/2015/03/02/dont-break-the-chain/
    public static <T> ObservableTransformer<T, T> applyObservableSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> applyFlowableSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer applyCompletableSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
