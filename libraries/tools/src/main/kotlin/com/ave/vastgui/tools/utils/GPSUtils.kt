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

@file:JvmName("GPSUtilsKt")

package com.ave.vastgui.tools.utils

import android.content.Context
import android.location.LocationManager
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.content.ContextHelper

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/13 17:41
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/hardware/gps-utils/

/**
 * Determine if GPS is turned on.
 *
 * @return True if GPS is turned on, false otherwise.
 * @since 0.5.3
 */
@Deprecated(
    level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith("isLocationEnabled()", "com.ave.vastgui.tools.utils.isLocationEnabled"),
    message = "Use the LocationManagerCompat instead."
)
fun isGPSOpen(): Boolean {
    val locationManager: LocationManager = cast(
        ContextHelper.getAppContext()
            .getSystemService(Context.LOCATION_SERVICE)
    )
    // Through GPS satellite positioning, the positioning level can be accurate to the street
    // (through 24 satellite positioning, the positioning is accurate and fast in outdoor and open places).
    val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    // Position determined by WLAN or mobile network (3G/2G) (also called AGPS, Assisted GPS Positioning.
    // Mainly used for positioning indoors or in densely covered places (buildings or dense deep forest, etc.).
    val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    return gps && network
}