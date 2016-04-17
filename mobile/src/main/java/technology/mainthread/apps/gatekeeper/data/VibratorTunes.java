package technology.mainthread.apps.gatekeeper.data;

import android.support.v4.util.ArrayMap;

public class VibratorTunes {

    private VibratorTunes() {
    }

    public static String KEY_STANDARD = "STANDARD";
    public static String KEY_STAR_WARS = "STAR_WARS";
    public static String KEY_SUPER_MARIO = "SUPER_MARIO";
    public static String KEY_JAMES_BOND = "JAMES_BOND";

    public static long[] STANDARD = new long[]{0, 100, 100, 100, 100};
    public static long[] STAR_WARS = new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500};
    public static long[] SUPER_MARIO = new long[]{0, 125, 75, 125, 275, 200, 275, 125, 75, 125, 275, 200, 600, 200, 600};
    public static long[] JAMES_BOND = new long[]{0, 200, 100, 200, 275, 425, 100, 200, 100, 200, 275, 425, 100, 75, 25, 75, 125, 75, 25, 75, 125, 100, 100};

    public static ArrayMap<String, long[]> VIBRATE_TUNES = getTunesMap();

    private static ArrayMap<String, long[]> getTunesMap() {
        ArrayMap<String, long[]> map = new ArrayMap<>();
        map.put(KEY_STANDARD, STANDARD);
        map.put(KEY_STAR_WARS, STAR_WARS);
        map.put(KEY_SUPER_MARIO, SUPER_MARIO);
        map.put(KEY_JAMES_BOND, JAMES_BOND);
        return map;
    }

}
