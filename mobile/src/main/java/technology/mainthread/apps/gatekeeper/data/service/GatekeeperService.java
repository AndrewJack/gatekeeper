package technology.mainthread.apps.gatekeeper.data.service;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceAction;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus;

public interface GatekeeperService {

    @GET()
    Flowable<Response<DeviceStatus>> deviceInfo(@Header("Authorization") String auth);

    @GET("isPrimed")
    Flowable<Response<DeviceStatus>> primedStatus(@Header("Authorization") String auth);

    @GET("isPrimed")
    Flowable<DeviceStatus> primedStatusResult(@Header("Authorization") String auth);

    @GET("isDoorOpen")
    Flowable<Response<DeviceStatus>> doorStatus(@Header("Authorization") String auth);

    @GET("isDoorOpen")
    Flowable<DeviceStatus> doorStatusResult(@Header("Authorization") String auth);

    @POST("unlock")
    Flowable<Response<DeviceAction>> unlock(@Header("Authorization") String auth);

    @POST("prime")
    Flowable<Response<DeviceAction>> prime(@Header("Authorization") String auth);

}
