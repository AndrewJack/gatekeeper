package technology.mainthread.apps.gatekeeper.util;

import android.content.Context;

import com.facebook.stetho.Stetho;

import okhttp3.OkHttpClient;

public class StethoUtil {

    private StethoUtil() {
    }

    public static void setUpStetho(Context context) {
        Stetho.initializeWithDefaults(context);
    }

    public static void addInterceptor(OkHttpClient.Builder builder) {
//        builder.addInterceptor(new StethoInterceptor());
    }
}
