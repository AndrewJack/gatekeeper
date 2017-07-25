package technology.mainthread.apps.gatekeeper.model.particle

import com.squareup.moshi.Json

class CoreInfo(val connected: Boolean, @Json(name = "last_heard") val lastHeard: String)
