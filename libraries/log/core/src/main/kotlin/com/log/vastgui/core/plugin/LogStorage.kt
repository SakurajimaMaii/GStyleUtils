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

import com.log.vastgui.core.LogPipeline
import com.log.vastgui.core.LogCat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogLevel.ASSERT
import com.log.vastgui.core.base.LogLevel.DEBUG
import com.log.vastgui.core.base.LogLevel.ERROR
import com.log.vastgui.core.base.LogLevel.INFO
import com.log.vastgui.core.base.LogLevel.VERBOSE
import com.log.vastgui.core.base.LogLevel.WARN
import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.base.LogStore
import com.log.vastgui.core.pipeline.PipelinePhase
import kotlin.properties.Delegates
import com.log.vastgui.core.base.allLogLevel as allLogLevel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/5
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/plugin/storage/

/**
 * [LogStorage].
 *
 * @since 0.5.3
 */
class LogStorage private constructor(private val mConfiguration: Configuration) {

    /**
     * Used to determine whether logs of this level are allowed to be storage.
     *
     * @since 1.3.4
     */
    private val mLevelMap: MutableMap<LogLevel, Boolean> = mutableMapOf(
        VERBOSE to false, DEBUG to false, INFO to false,
        WARN to false, ERROR to false, ASSERT to false
    )

    /**
     * [LogStorage] configuration.
     *
     * @property level Minimum priority of the log.
     * @property levelSet Log levels allowed to be stored.
     * @property logStore Log store implementation.
     * @since 1.3.1
     */
    class Configuration internal constructor() {
        @Deprecated(message = "Use levelList instead.", level = DeprecationLevel.WARNING)
        var level: LogLevel = VERBOSE

        var levelSet: Set<LogLevel> = emptySet()

        var logStore: LogStore by Delegates.notNull()
    }

    /** @since 1.3.1 */
    private val mLogStore: LogStore
        get() = mConfiguration.logStore

    /**
     * Store log.
     *
     * @since 1.3.1
     */
    private fun storeLog(logInfo: LogInfo) {
        mLogStore.store(logInfo)
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

    companion object : LogPlugin<Configuration, LogStorage> {

        override val key: String = LogStorage::class.java.simpleName

        override fun install(plugin: LogStorage, scope: LogCat) {
            scope.logPipeline.intercept(LogPipeline.State) {
                if (plugin.mLevelMap[subject.level] == false) {
                    finish()
                }
            }
            val store = PipelinePhase("Store")
            scope.logPipeline.insertPhaseAfter(LogPipeline.Output, store)
            scope.logPipeline.intercept(store) {
                plugin.storeLog(subject.logInfo)
                proceed()
            }
        }

        override fun configuration(config: Configuration.() -> Unit): LogStorage {
            val configuration = Configuration().also(config)
            return LogStorage(configuration)
        }

    }

}