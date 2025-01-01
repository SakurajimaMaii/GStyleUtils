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

@file:JvmName("BuildExt")

package com.ave.vastgui.tools.os.extension

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/3/27 23:59
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/os/extension/build/

/** @since 1.5.2 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
inline fun fromApi35(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.VANILLA_ICE_CREAM, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
inline fun fromApi34(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
inline fun fromApi33(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.TIRAMISU, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
inline fun fromApi32(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.S_V2, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
inline fun fromApi31(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.S, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
inline fun fromApi30(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.R, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
inline fun fromApi29(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.Q, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
inline fun fromApi28(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.P, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
inline fun fromApi27(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.O_MR1, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
inline fun fromApi26(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.O, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
inline fun fromApi25(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.N_MR1, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
inline fun fromApi24(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.N, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
inline fun fromApi23(action: () -> Unit) {
    fromApi(Build.VERSION_CODES.M, action)
}

/** @since 1.2.1 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
inline fun fromApi(version: Int, action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= version) {
        action()
    }
}