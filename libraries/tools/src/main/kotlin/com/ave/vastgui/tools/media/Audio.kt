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

package com.ave.vastgui.tools.media

import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.ave.vastgui.tools.content.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/3 9:53

/**
 * Audio manager
 *
 * @since 1.5.2
 */
fun getAudioManager(): AudioManager? = ContextCompat
    .getSystemService(ContextHelper.getAppContext(), AudioManager::class.java)

/**
 * Determine whether the device includes at least one form of audio output.
 *
 * @see PackageManager.getSystemAvailableFeatures
 * @since 1.5.0
 */
fun isFeatureAudioOutput(): Boolean {
    val context = ContextHelper.getAppContext()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // FIXME Refer to https://blog.csdn.net/qq_37858386/article/details/124313730 and the
        //  information obtained from PackageManager.getSystemAvailableFeatures. The version
        //  here should be 0.
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT, 0)
    } else {
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)
    }
}

/**
 * Determine whether a wired headset is connected or not.
 *
 * @since 1.5.0
 */
fun isWiredHeadsetOn(): Boolean {
    val audioManager = getAudioManager()
    if (!isFeatureAudioOutput() || null == audioManager) return false
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            .any { AudioDeviceInfo.TYPE_WIRED_HEADSET == it.type }
    } else {
        audioManager.isWiredHeadsetOn
    }
}