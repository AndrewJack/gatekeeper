package technology.mainthread.apps.gatekeeper.data.service;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceAction;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus;

public interface GatekeeperService {

    @GET()
    Observable<Response<DeviceStatus>> deviceInfo(@Header("Authorization") String auth);

    @GET("isPrimed")
    Observable<Response<DeviceStatus>> primedStatus(@Header("Authorization") String auth);

    @GET("isPrimed")
    Observable<DeviceStatus> primedStatusResult(@Header("Authorization") String auth);

    @GET("isDoorOpen")
    Observable<Response<DeviceStatus>> doorStatus(@Header("Authorization") String auth);

    @GET("isDoorOpen")
    Observable<DeviceStatus> doorStatusResult(@Header("Authorization") String auth);

    @POST("unlock")
    Observable<Response<DeviceAction>> unlock(@Header("Authorization") String auth);

    @POST("prime")
    Observable<Response<DeviceAction>> prime(@Header("Authorization") String auth);

}
