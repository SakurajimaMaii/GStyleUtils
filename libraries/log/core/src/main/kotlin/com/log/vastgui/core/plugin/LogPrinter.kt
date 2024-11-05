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

package com.log.vastgui.core.plugin

import com.log.vastgui.core.LogCat
import com.log.vastgui.core.LogPipeline
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogLevel.ASSERT
import com.log.vastgui.core.base.LogLevel.DEBUG
import com.log.vastgui.core.base.LogLevel.ERROR
import com.log.vastgui.core.base.LogLevel.INFO
import com.log.vastgui.core.base.LogLevel.VERBOSE
import com.log.vastgui.core.base.LogLevel.WARN
import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.base.Logger
import com.log.vastgui.core.base.allLogLevel
import com.log.vastgui.core.base.default

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/plugin/printer/

/**
 * LogPrinter.
 *
 * @property mConfiguration The configuration of [LogPrinter].
 * @since 0.5.3
 */
class LogPrinter private constructor(private val mConfiguration: Configuration) {

    /**
     * Used to determine whether logs of this level are allowed to be printed.
     *
     * @since 1.3.4
     */
    private val mLevelMap: MutableMap<LogLevel, Boolean> = mutableMapOf(
        VERBOSE to false, DEBUG to false, INFO to false,
        WARN to false, ERROR to false, ASSERT to false
    )

    /**
     * [LogPrinter] configuration.
     *
     * @property level Minimum priority of the log.
     * @property levelSet Log levels allowed to be printed.
     * @property logger Log printing implementation.
     * @since 1.3.1
     */
    class Configuration internal constructor() {
        @Deprecated(message = "Use levelList instead.", level = DeprecationLevel.WARNING)
        var level: LogLevel = VERBOSE

        var levelSet: Set<LogLevel> = emptySet()

        var logger: Logger = Logger.default()
    }

    /** @since 1.3.1 */
    private val mLogger: Logger = mConfiguration.logger

    /**
     * Print log.
     *
     * @since 1.3.1
     */
    private fun printLog(logInfo: LogInfo) {
        mLogger.log(logInfo)
    }

    init {
        // Use level
        if (mConfiguration.levelSet.isEmpty()) {
            allLogLevel.filter { level -> level >= mConfiguration.level }
                .forEach { mLevelMap[it] = true }
        }
        // Use levelList
        else {
            mConfiguration.levelSet.forEach {
                mLevelMap[it] = true
            }
        }
    }

    companion object : LogPlugin<Configuration, LogPrinter> {
        override val key: String = LogPrinter::class.java.simpleName

        override fun configuration(config: Configuration.() -> Unit): LogPrinter {
            val configuration = Configuration().apply(config)
            return LogPrinter(configuration)
        }

        override fun install(plugin: LogPrinter, scope: LogCat) {
            scope.logPipeline.intercept(LogPipeline.State) {
                if (plugin.mLevelMap[subject.level] == false) {
                    finish()
                }
            }
            scope.logPipeline.intercept(LogPipeline.Output) {
                val logInfo = subject.build()
                plugin.printLog(logInfo)
                proceed()
            }
        }
    }
}