package technology.mainthread.apps.gatekeeper.data.service;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;
import technology.mainthread.apps.gatekeeper.BuildConfig;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceAction;
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus;

public interface GatekeeperService {

    @Headers({BuildConfig.PARTICLE_AUTH})
    @GET()
    Observable<Response<DeviceStatus>> deviceInfo();

    @Headers({BuildConfig.PARTICLE_AUTH})
    @GET("isPrimed")
    Observable<Response<DeviceStatus>> primedStatus();

    @Headers({BuildConfig.PARTICLE_AUTH})
    @GET("isPrimed")
    Observable<DeviceStatus> primedStatusResult();

    @Headers({BuildConfig.PARTICLE_AUTH})
    @GET("doorStatus")
    Observable<Response<DeviceStatus>> doorStatus();

    @Headers({BuildConfig.PARTICLE_AUTH})
    @GET("doorStatus")
    Observable<DeviceStatus> doorStatusResult();

    @Headers({BuildConfig.PARTICLE_AUTH})
    @POST("doorOpen")
    Observable<Response<DeviceAction>> unlock();

    @Headers({BuildConfig.PARTICLE_AUTH})
    @POST("primeSystem")
    Observable<Response<DeviceAction>> prime();

}
