package technology.mainthread.apps.gatekeeper.data

import timber.log.Timber

class CrashTree : Timber.Tree() {

    override fun e(message: String, vararg args: Any) {
    }

    override fun e(throwable: Throwable, message: String, vararg args: Any) {
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

    }

}
