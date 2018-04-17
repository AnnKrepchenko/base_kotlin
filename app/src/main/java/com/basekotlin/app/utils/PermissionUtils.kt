package com.basekotlin.app.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.basekotlin.app.BuildConfig


import java.util.LinkedList


/**
 * Utils for Android >=6.0(M) permission requests
 */
object PermissionUtils {

    val PERMISSION_CODE_CAMERA = 1001
    val PERMISSION_CODE_READ_EXTERNAL_STORAGE = 1002
    val PERMISSION_CODE_WRITE_EXTERNAL_STORAGE = 1003
    val PERMISSION_CODE_RECORD_AUDIO = 1004
    private val TAG = "PermissionUtils"

    private fun requestPermissions(activity: Activity, permissionCode: Int, vararg permissions: String) {
        val neededPermissions = LinkedList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity.applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission)
            }
        }

        if (neededPermissions.isEmpty())
            return

        ActivityCompat.requestPermissions(
                activity,
                neededPermissions.toTypedArray(),
                permissionCode
        )
    }

    fun isPermissionAdded(activity: Activity, vararg permissionsArray: String): Boolean {
        var result = true
        for (permission in permissionsArray) {
            val permissionCheck = ContextCompat.checkSelfPermission(activity, permission)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                result = false
                break
            }
        }

        return result
    }

    fun extraPermissions(activity: Activity, permissionCode: Int, vararg permissions: String) {
        if (BuildConfig.DEBUG) Log.d(TAG, "extraPermissions")
        requestPermissions(activity, permissionCode, *permissions)
    }

    fun isPermissionGranted(permissions: Array<String>, grantResults: IntArray): Boolean {
        for (i in permissions.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun openDetailedAppSettings(packageName: String): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        return intent
    }
}

