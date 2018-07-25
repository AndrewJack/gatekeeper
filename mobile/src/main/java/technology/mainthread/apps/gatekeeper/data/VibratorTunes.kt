package technology.mainthread.apps.gatekeeper.data

import androidx.collection.ArrayMap

object VibratorTunes {

    var KEY_STANDARD = "STANDARD"
    var KEY_STAR_WARS = "STAR_WARS"
    var KEY_SUPER_MARIO = "SUPER_MARIO"
    var KEY_JAMES_BOND = "JAMES_BOND"

    var STANDARD = longArrayOf(0, 100, 100, 100, 100)
    var STAR_WARS = longArrayOf(0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500)
    var SUPER_MARIO = longArrayOf(0, 125, 75, 125, 275, 200, 275, 125, 75, 125, 275, 200, 600, 200, 600)
    var JAMES_BOND = longArrayOf(0, 200, 100, 200, 275, 425, 100, 200, 100, 200, 275, 425, 100, 75, 25, 75, 125, 75, 25, 75, 125, 100, 100)

    var VIBRATE_TUNES = tunesMap

    private val tunesMap: ArrayMap<String, LongArray>
        get() {
            val map = ArrayMap<String, LongArray>()
            map.put(KEY_STANDARD, STANDARD)
            map.put(KEY_STAR_WARS, STAR_WARS)
            map.put(KEY_SUPER_MARIO, SUPER_MARIO)
            map.put(KEY_JAMES_BOND, JAMES_BOND)
            return map
        }

}
