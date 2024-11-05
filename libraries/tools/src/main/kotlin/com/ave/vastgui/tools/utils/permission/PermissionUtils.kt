/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.tools.utils.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.ave.vastgui.tools.utils.AppUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/5
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/permission/permission/

const val DENIED = "DENIED"
const val EXPLAINED = "EXPLAINED"

/**
 * Allows an application to read the user's calendar data.
 *
 * @since 0.2.0
 */
const val DATE = Manifest.permission.READ_CALENDAR

/**
 * Required to be able to access the camera device.
 *
 * @since 0.2.0
 */
const val CAMERA = Manifest.permission.CAMERA

/** Allows an application to read the user's contacts data. */
const val PEOPLE = Manifest.permission.READ_CONTACTS

/**
 * Allows an application to record audio.
 *
 * @since 0.2.0
 */
const val AUDIO = Manifest.permission.RECORD_AUDIO

/**
 * Allows read only access to phone state, make calls, obtain and modify
 * call records, allow video calls, allow surveillance, modify or abandon
 * broadcast calls.
 *
 * @since 0.2.0
 */
const val PHONE = Manifest.permission.READ_PHONE_STATE

/**
 * Allows an application to read and modify SMS, send SMS, receive SMS
 * content and SMS broadcast, receive MMS.
 *
 * @since 0.2.0
 */
const val SMS = Manifest.permission.READ_SMS

/**
 * Allows an application to write to external storage.
 *
 * @since 0.2.0
 */
const val SD = Manifest.permission.WRITE_EXTERNAL_STORAGE

/**
 * Allows an application to read image files from external storage.
 *
 * @since 0.2.0
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val SYSTEM_IMAGE = Manifest.permission.READ_MEDIA_IMAGES

/**
 * Allows an application to read video files from external storage.
 *
 * @since 0.2.0
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val SYSTEM_VIDEO = Manifest.permission.READ_MEDIA_VIDEO

/**
 * Allows an application to read audio files from external storage.
 *
 * @since 0.2.0
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val SYSTEM_AUDIO = Manifest.permission.READ_MEDIA_AUDIO


/**
 * Allows an application to access data from sensors that the user uses to
 * measure what is happening inside their body, such as heart rate.
 *
 * @since 0.2.0
 */
const val SENSORS = Manifest.permission.BODY_SENSORS

/**
 * Activity requestPermission.
 *
 * @param permission Request permission name.
 * @param builder [PermissionBuilder].
 * @since 0.2.0
 */
inline fun ComponentActivity.requestPermission(
    permission: String,
    builder: PermissionBuilder.() -> Unit = {}
) {
    if (permission.isEmpty()) return
    val mBuilder = PermissionBuilder().also(builder)
    singlePermissionLauncher()?.launch(permission) { result ->
        when {
            result -> mBuilder.granted(permission)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                if (shouldShowRequestPermissionRationale(permission)) {
                    mBuilder.denied(permission)
                }
            }

            else -> mBuilder.noMoreAsk(permission)
        }
    }
}

/**
 * Activity requestMultiplePermissions.
 *
 * @param permissions Request permissions name.
 * @param builder [MultiPermissionBuilder].
 * @since 0.2.0
 */
inline fun ComponentActivity.requestMultiplePermissions(
    permissions: Array<String>,
    builder: MultiPermissionBuilder.() -> Unit = {}
) {
    val mBuilder = MultiPermissionBuilder().also(builder)
    val declaredPermissions = packageManager
        .getPackageInfo(AppUtils.getPackageName(), PackageManager.GET_PERMISSIONS)
        .requestedPermissions?.toSet() ?: emptySet()
    val requestPermissions = permissions.filter { !declaredPermissions.contains(it) }
    if (requestPermissions.isNotEmpty()) {
        mBuilder.noDeclare(requestPermissions)
        return
    }
    multiPermissionLauncher()?.launch(permissions) { result: Map<String, Boolean> ->
        val deniedList = result.filter { !it.value }.map { it.key }
        when {
            deniedList.isNotEmpty() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val map = deniedList.groupBy { permission ->
                        if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                    }
                    map[DENIED]?.let { mBuilder.denied(it) }
                    map[EXPLAINED]?.let { mBuilder.noMoreAsk(it) }
                } else {
                    mBuilder.denied(deniedList)
                    mBuilder.noMoreAsk(emptyList())
                }
            }

            else -> mBuilder.allGranted()
        }
    }
}

/**
 * Fragment requestPermission.
 *
 * @param permission Request permission name.
 * @param builder [PermissionBuilder].
 * @since 0.2.0
 */
inline fun Fragment.requestPermission(
    permission: String,
    builder: PermissionBuilder.() -> Unit = {}
) {
    if (permission.isEmpty()) return
    val mBuilder = PermissionBuilder().also(builder)
    requireActivity().singlePermissionLauncher()?.launch(permission) { result ->
        when {
            result -> mBuilder.granted(permission)
            shouldShowRequestPermissionRationale(permission) -> mBuilder.denied(permission)
            else -> mBuilder.noMoreAsk(permission)
        }
    }
}

/**
 * Fragment requestMultiplePermissions.
 *
 * @param permissions Request permissions name.
 * @param builder [MultiPermissionBuilder].
 * @since 0.2.0
 */
inline fun Fragment.requestMultiplePermissions(
    permissions: Array<String>,
    builder: MultiPermissionBuilder.() -> Unit = {}
) {
    val mBuilder = MultiPermissionBuilder().also(builder)
    val declaredPermissions = requireActivity().packageManager
        .getPackageInfo(AppUtils.getPackageName(), PackageManager.GET_PERMISSIONS)
        .requestedPermissions?.toSet() ?: emptySet()
    val requestPermissions = permissions.filter { !declaredPermissions.contains(it) }
    if (requestPermissions.isNotEmpty()) {
        mBuilder.noDeclare(requestPermissions)
        return
    }
    requireActivity().multiPermissionLauncher()
        ?.launch(permissions) { result: Map<String, Boolean> ->
            val deniedList = result.filter { !it.value }.map { it.key }
            when {
                deniedList.isNotEmpty() -> {
                    val map = deniedList.groupBy { permission ->
                        if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                    }
                    map[DENIED]?.let { mBuilder.denied(it) }
                    map[EXPLAINED]?.let { mBuilder.noMoreAsk(it) }
                }

                else -> mBuilder.allGranted()
            }
        }
}

/**
 * PermissionBuilder.
 *
 * @property granted Permission is granted.
 * @property denied Denied but can request again.
 * @property noMoreAsk Denied and can't request again.
 * @since 0.2.0
 */
class PermissionBuilder {
    var granted: (permission: String) -> Unit = {}
    var denied: (permission: String) -> Unit = {}
    var noMoreAsk: (permission: String) -> Unit = {}
}

/**
 * MultiPermissionBuilder.
 *
 * @since 0.2.0
 */
class MultiPermissionBuilder {
    /**
     * All permissions is granted.
     *
     * @since 0.2.0
     */
    var allGranted: () -> Unit = {}

    /**
     * Denied but can request again.
     *
     * @since 0.2.0
     */
    var denied: (List<String>) -> Unit = {}

    /**
     * Denied and can't request again.
     *
     * @since 0.2.0
     */
    var noMoreAsk: (List<String>) -> Unit = {}

    /**
     * The permission are not declared in AndroidManifest.xml.
     *
     * @since 0.5.6
     */
    var noDeclare: (List<String>) -> Unit = {}
}