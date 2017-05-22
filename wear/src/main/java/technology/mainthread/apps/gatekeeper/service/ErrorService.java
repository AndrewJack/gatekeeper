package technology.mainthread.apps.gatekeeper.service;

import android.content.Intent;
import android.os.Build;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.DaggerIntentService;
import technology.mainthread.apps.gatekeeper.common.SharedValues;
import technology.mainthread.apps.gatekeeper.injector.WearAppClient;
import timber.log.Timber;

public class ErrorService extends DaggerIntentService {

    @Inject
    @WearAppClient
    GoogleApiClient mGoogleAppClient;

    public ErrorService() {
        super(ErrorService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mGoogleAppClient.blockingConnect(SharedValues.CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);

        List<String> nodes = getNodes(mGoogleAppClient);
        if (nodes.isEmpty()) {
            return;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(intent.getSerializableExtra("exception"));

            byte[] exceptionData = bos.toByteArray();
            DataMap dataMap = new DataMap();

            dataMap.putString("board", Build.BOARD);
            dataMap.putString("fingerprint", Build.FINGERPRINT);
            dataMap.putString("model", Build.MODEL);
            dataMap.putString("manufacturer", Build.MANUFACTURER);
            dataMap.putString("product", Build.PRODUCT);
            dataMap.putByteArray(SharedValues.KEY_WEAR_EXCEPTION, exceptionData);

            Wearable.MessageApi.sendMessage(mGoogleAppClient, nodes.get(0), SharedValues.PATH_WEAR_ERROR, dataMap.toByteArray());

        } catch (Exception e) {
            Timber.w(e, "Failed sending error to phone");
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                Timber.e(e, "Object output stream close exception");
            }
            try {
                bos.close();
            } catch (IOException e) {
                Timber.e(e, "Byte array output stream close exception");
            }
        }
    }

    private List<String> getNodes(GoogleApiClient mGoogleApiClient) {
        ArrayList<String> results = new ArrayList<>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }

}