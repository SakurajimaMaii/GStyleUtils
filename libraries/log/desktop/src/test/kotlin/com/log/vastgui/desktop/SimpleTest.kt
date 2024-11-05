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

package com.log.vastgui.desktop

import org.junit.Test

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/14 23:59

class SimpleTest {
    private val logcat = logFactory("SimpleTest")

    @Test
    fun logTest() {
        val map = mapOf("name" to "Xiao Ming", "age" to 19)
        logcat.d(map)
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        logcat.i(list)
        logcat.w("This is a log.")
        logcat.e("This is a log.")
        logcat.a("This is a log.")
    }

}