package technology.mainthread.apps.gatekeeper.rx

import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// http://blog.danlew.net/2015/03/02/dont-break-the-chain/
@JvmName("RxSchedulerHelper")

fun <T> applyObservableSchedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> applyFlowableSchedulers(): FlowableTransformer<T, T> {
    return FlowableTransformer { observable ->
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun applyCompletableSchedulers(): CompletableTransformer {
    return CompletableTransformer { observable ->
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
