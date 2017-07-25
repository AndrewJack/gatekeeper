package technology.mainthread.apps.gatekeeper.data

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

import javax.inject.Inject

import timber.log.Timber

class AndroidAppInfo @Inject
internal constructor(private val context: Context) {

    val versionName: String
        get() {
            var versionName = ""
            val packageInfo = packageInfo
            if (packageInfo != null) {
                versionName = packageInfo.versionName
            }
            return versionName
        }

    val versionCode: Int
        get() {
            var versionCode = 0
            val packageInfo = packageInfo
            if (packageInfo != null) {
                versionCode = packageInfo.versionCode
            }
            return versionCode
        }

    private val packageInfo: PackageInfo?
        get() {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.w(e, "package info not found")
            }

            return packageInfo
        }
}