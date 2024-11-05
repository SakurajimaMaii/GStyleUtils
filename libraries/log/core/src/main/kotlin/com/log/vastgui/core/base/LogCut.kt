/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.log.vastgui.core.base

import com.log.vastgui.core.annotation.LogApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/7

/**
 * Return true if the content of the [LogInfo] does not need to be split,
 * false otherwise.
 *
 * @since 0.5.3
 */
@LogApi
fun LogInfo.needCut(maxSingleLogLength: Int) =
    !(printLength < maxSingleLogLength || (maxSingleLogLength * 4) >= printBytesLength)

/**
 * Cutting the byte array as a string according to [maxSingleLogLength].
 *
 * @return The string obtained by [maxSingleLogLength].
 * @since 0.5.3
 */
@LogApi
fun ByteArray.cutStr(maxSingleLogLength: Int): String {

    // Return when the bytes length is less than the subLength.
    if (maxSingleLogLength * 4 >= size) {
        return String(this)
    }

    // Copy the fixed-length byte array and convert it to a string
    val subStr = String(this.copyOf(maxSingleLogLength * 4))

    // Avoid the end character is split, here minus 1 to keep the string intact
    return subStr.substring(0, subStr.length - 1)
}