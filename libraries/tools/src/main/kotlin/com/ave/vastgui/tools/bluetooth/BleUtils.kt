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

package com.ave.vastgui.tools.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.ave.vastgui.tools.content.ContextHelper
import com.ave.vastgui.tools.os.extension.fromApi31

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/12/31

/**
 * Get [BluetoothManager].
 *
 * @since 1.5.2
 */
fun getBluetoothManager(): BluetoothManager? {
    return ContextCompat
        .getSystemService(ContextHelper.getAppContext(), BluetoothManager::class.java)
}

/**
 * Return true if Bluetooth is currently enabled and ready for use.
 *
 * @since 1.5.2
 */
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun isBleEnabled(): Boolean {
    fromApi31 {
        val bluetoothManager = getBluetoothManager() ?: return false
        return bluetoothManager.adapter.isEnabled
    }
    @Suppress("DEPRECATION")
    return BluetoothAdapter.getDefaultAdapter().isEnabled
}