package technology.mainthread.apps.gatekeeper.data.service

import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import technology.mainthread.apps.gatekeeper.model.particle.DeviceAction
import technology.mainthread.apps.gatekeeper.model.particle.DeviceStatus

interface GatekeeperService {

    @GET
    fun deviceInfo(@Header("Authorization") auth: String): Flowable<Response<DeviceStatus>>

    @GET("isPrimed")
    fun primedStatus(@Header("Authorization") auth: String): Flowable<Response<DeviceStatus>>

    @GET("isPrimed")
    fun primedStatusResult(@Header("Authorization") auth: String): Flowable<DeviceStatus>

    @GET("isDoorOpen")
    fun doorStatus(@Header("Authorization") auth: String): Flowable<Response<DeviceStatus>>

    @GET("isDoorOpen")
    fun doorStatusResult(@Header("Authorization") auth: String): Flowable<DeviceStatus>

    @POST("unlock")
    fun unlock(@Header("Authorization") auth: String): Flowable<Response<DeviceAction>>

    @POST("prime")
    fun prime(@Header("Authorization") auth: String): Flowable<Response<DeviceAction>>

}
