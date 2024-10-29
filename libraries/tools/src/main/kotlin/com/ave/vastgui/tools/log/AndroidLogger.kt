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

package com.ave.vastgui.tools.log

import android.util.Log
import com.ave.vastgui.tools.log.base.LogScope
import com.ave.vastgui.tools.log.base.LogScope.ExceptionLog
import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.Logger
import com.log.vastgui.core.format.DEFAULT_MAX_PRINT_TIMES
import com.log.vastgui.core.format.DEFAULT_MAX_SINGLE_LOG_LENGTH
import com.log.vastgui.core.format.TableFormat
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/13 20:48
// Documentation: https://ave.entropy2020.cn/documents/tools/log/logger/

/**
 * Android Logger.
 *
 * ```kotlin
 * val mLogFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogPrinter) {
 *         logger = Logger.android(30,5)
 *     }
 * }
 * ```
 *
 * @param maxSingleLogLength The max length of single line of log. every
 * char in line is calculated as 4 bytes.
 * @param maxPrintTimes The total repeat number of prints. For example, the
 * log content is divided into ten lines depending on maxSingleLogLength.
 * If you set maxPrintTimes to 5, only the first five lines will be
 * printed.
 * @see <img
 * src="https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/tools/image/log.png?raw=true">
 * @since 1.3.1
 */
fun Logger.Companion.android(
    maxSingleLogLength: Int = DEFAULT_MAX_SINGLE_LOG_LENGTH,
    maxPrintTimes: Int = DEFAULT_MAX_PRINT_TIMES,
    header: TableFormat.LogHeader =
        TableFormat.LogHeader(thread = true, tag = true, level = true, time = true)
): AndroidLogger = AndroidLogger(TableFormat(maxSingleLogLength, maxPrintTimes, header))

/**
 * Android Logger with custom [LogFormat].
 *
 * @since 1.5.0
 */
fun Logger.Companion.android(logFormat: LogFormat): AndroidLogger = AndroidLogger(logFormat)

/**
 * Android logger.
 *
 * @since 1.3.1
 */
class AndroidLogger internal constructor(
    override val logFormat: LogFormat
) : LogScope(), Logger {

    override fun log(logInfo: LogInfo) {
        mLogScope.launch { mLogChannel.send(logInfo) }
    }

    init {
        mLogScope.launch {
            while (isActive) {
                val info = mLogChannel.receive()
                Log.println(info.levelPriority, info.tag, logFormat.format(info))
            }
        }

        exceptionLog = ExceptionLog { _, exception ->
            val threadName = Thread.currentThread().name
            val info = LogInfo(threadName, exception.stackTrace[0], LogLevel.ERROR, TAG,
                System.currentTimeMillis(), exception.stackTraceToString(), exception)
            Log.println(info.levelPriority, info.tag, logFormat.format(info))
        }
    }

    companion object {
        const val TAG = "AndroidLogger"
    }
}