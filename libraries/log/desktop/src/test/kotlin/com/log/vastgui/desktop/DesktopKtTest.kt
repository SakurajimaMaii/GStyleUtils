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

import com.log.vastgui.core.LogFactory
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogStore
import com.log.vastgui.core.base.Logger
import com.log.vastgui.core.getLogFactory
import com.log.vastgui.core.json.GsonConverter
import com.log.vastgui.core.plugin.LogJson
import com.log.vastgui.core.plugin.LogPrinter
import com.log.vastgui.core.plugin.LogStorage
import com.log.vastgui.core.plugin.LogSwitch
import com.log.vastgui.desktop.format.LineColorfulFormat
import org.junit.Test

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/14 23:59

class DesktopKtTest {
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

    /**
     * [Issue
     * 174](https://github.com/SakurajimaMaii/Android-Vast-Extension/issues/174)
     */
    @Test
    fun issue174() {
        val logFactory: LogFactory = getLogFactory {
            install(LogSwitch) {
                open = true
            }
            install(LogPrinter) {
                logger = Logger.desktop(LineColorfulFormat)
                levelSet = setOf(LogLevel.WARN, LogLevel.INFO)
            }
            install(LogStorage) {
                logStore = LogStore.desktop("", 1024L * 1000)
                levelSet = setOf(LogLevel.DEBUG, LogLevel.ASSERT)
            }
            install(LogJson) {
                converter = GsonConverter.getInstance(true)
            }
        }

        val log = logFactory("SimpleTest")
        val map = mapOf("name" to "Xiao Ming", "age" to 19)
        log.d(map)
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        log.i(list)
        log.w("This is a log.")
        log.e("This is a log.")
        log.a("This is a log.")
    }

}