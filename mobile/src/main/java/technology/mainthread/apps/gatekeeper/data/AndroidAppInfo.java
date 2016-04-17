package technology.mainthread.apps.gatekeeper.data;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import javax.inject.Inject;

import timber.log.Timber;

public class AndroidAppInfo {

    private final Context context;

    @Inject
    public AndroidAppInfo(Context context) {
        this.context = context;
    }

    public String getVersionName() {
        String versionName = "";
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            versionName = packageInfo.versionName;
        }
        return versionName;
    }

    public int getVersionCode() {
        int versionCode = 0;
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            versionCode = packageInfo.versionCode;
        }
        return versionCode;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.w(e, "package info not found");
        }
        return packageInfo;
    }
}