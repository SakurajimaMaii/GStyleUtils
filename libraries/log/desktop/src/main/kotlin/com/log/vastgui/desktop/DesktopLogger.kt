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

import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.Logger
import com.log.vastgui.desktop.format.LineColorfulFormat

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/14 23:04
// Documentation: https://ave.entropy2020.cn/documents/log/log-desktop/usage/

/**
 * Desktop logger.
 *
 * @param logFormat Used to set the log style.
 *
 * ```kotlin
 * val logFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogPrinter) {
 *         logger = Logger.desktop()
 *     }
 * }
 * ```
 *
 * @since 1.3.1
 */
fun Logger.Companion.desktop(logFormat: LogFormat = LineColorfulFormat): DesktopLogger =
    DesktopLogger(logFormat)

/**
 * Desktop logger.
 *
 * @since 1.3.1
 */
class DesktopLogger internal constructor(override val logFormat: LogFormat) : Logger {

    override fun log(logInfo: LogInfo) {
        println(logFormat.format(logInfo))
    }

}