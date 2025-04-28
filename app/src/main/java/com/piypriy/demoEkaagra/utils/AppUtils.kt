package com.piypriy.demoEkaagra.utils

import android.content.Context
import android.content.pm.PackageManager

data class AppInfo(val name: String, val packageName: String)

fun getThirdPartyApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

    return apps.filter { appInfo ->
        (appInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0
    }.map { appInfo ->
        AppInfo(
            name = pm.getApplicationLabel(appInfo).toString(),
            packageName = appInfo.packageName
        )
    }.sortedBy { it.name }
}
