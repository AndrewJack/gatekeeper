package technology.mainthread.apps.gatekeeper.messenger

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import technology.mainthread.apps.gatekeeper.common.CONNECTION_TIME_OUT_MS
import technology.mainthread.apps.gatekeeper.injector.WearAppClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WearableMessenger @Inject
constructor(@param:WearAppClient private val googleApiClient: GoogleApiClient) {

    /**
     * Send a wearable message using the MessageApi

     * @param path message identifier for the receiver
     * *
     * @param data payload
     */
    fun sendMessage(path: String, data: ByteArray): Boolean {
        return sendMessageInternal(path, data)
    }

    private fun sendMessageInternal(path: String, data: ByteArray): Boolean {
        var result = false
        if (isConnected) {
            val nodeId = pickBestNodeId(nodes)
            val pendingResult = Wearable.MessageApi.sendMessage(googleApiClient, nodeId, path, data)

            val messageResult = pendingResult.await()
            result = messageResult.status.isSuccess
        }
        disconnect()

        return result
    }

    private val isConnected: Boolean
        get() {
            if (googleApiClient.isConnected) {
                return true
            } else {
                val connectionResult = googleApiClient.blockingConnect(
                        CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
                return connectionResult.isSuccess
            }
        }

    private fun disconnect() {
        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
    }

    private val nodes: List<Node>
        get() {
            val nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await()
            return nodes.nodes
        }

    private fun pickBestNodeId(nodes: List<Node>): String? {
        var bestNodeId: String? = null
        // Find a nearby node or pick one arbitrarily
        for (node in nodes) {
            if (node.isNearby) {
                return node.id
            }
            bestNodeId = node.id
        }
        return bestNodeId
    }

}
