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

package com.ave.vastgui.tools.location

import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.ave.vastgui.tools.content.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/12/31

/**
 * Get [LocationManager].
 *
 * @since 1.5.2
 */
fun getLocationManager(): LocationManager? {
    return ContextCompat
        .getSystemService(ContextHelper.getAppContext(), LocationManager::class.java)
}

/**
 * Is location enabled.
 *
 * @since 1.5.2
 */
fun isLocationEnabled(): Boolean {
    val locationManager = ContextCompat
        .getSystemService(ContextHelper.getAppContext(), LocationManager::class.java)
        ?: return false
    return LocationManagerCompat.isLocationEnabled(locationManager)
}