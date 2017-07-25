package technology.mainthread.apps.gatekeeper.messenger

import io.reactivex.BackpressureStrategy
import io.reactivex.Emitter
import io.reactivex.Flowable
import technology.mainthread.apps.gatekeeper.common.PATH_PRIME
import technology.mainthread.apps.gatekeeper.common.PATH_UNLOCK
import javax.inject.Inject

class WearToMobileRequester @Inject
constructor(private val messenger: WearableMessenger) {

    fun prime(): Flowable<Boolean> {
        val source = { e: Emitter<Boolean> ->
            e.onNext(messenger.sendMessage(PATH_PRIME, ByteArray(0)))
            e.onComplete()
        }
        return Flowable.create<Boolean>(source, BackpressureStrategy.BUFFER)
    }

    fun unlock(): Flowable<Boolean> {
        val source = { e: Emitter<Boolean> ->
            e.onNext(messenger.sendMessage(PATH_UNLOCK, ByteArray(0)))
            e.onComplete()
        }
        return Flowable.create<Boolean>(source, BackpressureStrategy.BUFFER)
    }
}
