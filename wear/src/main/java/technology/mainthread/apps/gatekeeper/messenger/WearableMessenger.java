package technology.mainthread.apps.gatekeeper.messenger;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.common.SharedValues;
import technology.mainthread.apps.gatekeeper.injector.WearAppClient;

public class WearableMessenger {

    private GoogleApiClient googleApiClient;

    @Inject
    public WearableMessenger(@WearAppClient GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    /**
     * Send a wearable message using the MessageApi
     *
     * @param path message identifier for the receiver
     * @param data payload
     */
    public boolean sendMessage(String path, byte[] data) {
        return sendMessageInternal(path, data);
    }

    private boolean sendMessageInternal(String path, byte[] data) {
        boolean result = false;
        if (isConnected()) {
            String nodeId = pickBestNodeId(getNodes());
            PendingResult<MessageApi.SendMessageResult> pendingResult =
                    Wearable.MessageApi.sendMessage(googleApiClient, nodeId, path, data);

            MessageApi.SendMessageResult messageResult = pendingResult.await();
            result = messageResult.getStatus().isSuccess();
        }
        disconnect();

        return result;
    }

    private boolean isConnected() {
        if (googleApiClient.isConnected()) {
            return true;
        } else {
            ConnectionResult connectionResult = googleApiClient.blockingConnect(
                    SharedValues.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
            return connectionResult.isSuccess();
        }
    }

    private void disconnect() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    private List<Node> getNodes() {
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
        return nodes.getNodes();
    }

    private String pickBestNodeId(List<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

}
