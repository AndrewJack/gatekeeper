package technology.mainthread.apps.gatekeeper.service

import android.content.Intent
import android.os.Build
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.Wearable
import dagger.android.DaggerIntentService
import technology.mainthread.apps.gatekeeper.common.CONNECTION_TIME_OUT_MS
import technology.mainthread.apps.gatekeeper.common.KEY_WEAR_EXCEPTION
import technology.mainthread.apps.gatekeeper.common.PATH_WEAR_ERROR
import technology.mainthread.apps.gatekeeper.injector.WearAppClient
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ErrorService : DaggerIntentService(ErrorService::class.java.simpleName) {

    @field:[Inject WearAppClient]
    internal lateinit var mGoogleAppClient: GoogleApiClient

    override fun onHandleIntent(intent: Intent?) {
        mGoogleAppClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)

        val nodes = getNodes(mGoogleAppClient)
        if (nodes.isEmpty()) {
            return
        }

        val bos = ByteArrayOutputStream()
        var oos: ObjectOutputStream? = null

        try {
            oos = ObjectOutputStream(bos)
            oos.writeObject(intent!!.getSerializableExtra("exception"))

            val exceptionData = bos.toByteArray()
            val dataMap = DataMap()

            dataMap.putString("board", Build.BOARD)
            dataMap.putString("fingerprint", Build.FINGERPRINT)
            dataMap.putString("model", Build.MODEL)
            dataMap.putString("manufacturer", Build.MANUFACTURER)
            dataMap.putString("product", Build.PRODUCT)
            dataMap.putByteArray(KEY_WEAR_EXCEPTION, exceptionData)

            Wearable.MessageApi.sendMessage(mGoogleAppClient, nodes[0], PATH_WEAR_ERROR, dataMap.toByteArray())

        } catch (e: Exception) {
            Timber.w(e, "Failed sending error to phone")
        } finally {
            try {
                if (oos != null) {
                    oos.close()
                }
            } catch (e: IOException) {
                Timber.e(e, "Object output stream close exception")
            }

            try {
                bos.close()
            } catch (e: IOException) {
                Timber.e(e, "Byte array output stream close exception")
            }

        }
    }

    private fun getNodes(mGoogleApiClient: GoogleApiClient): List<String> {
        val results = ArrayList<String>()
        val nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await()
        for (node in nodes.nodes) {
            results.add(node.id)
        }
        return results
    }

}