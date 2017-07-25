package technology.mainthread.apps.gatekeeper.model.particle

import com.squareup.moshi.Json

class DeviceAction(@Json(name = "return_value") val returnValue: Int, val connected: Boolean)
