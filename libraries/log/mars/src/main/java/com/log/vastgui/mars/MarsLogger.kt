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

package com.log.vastgui.mars

import com.ave.vastgui.core.extension.SingletonHolder
import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.Logger
import com.log.vastgui.core.format.DEFAULT_MAX_PRINT_TIMES
import com.log.vastgui.core.format.DEFAULT_MAX_SINGLE_LOG_LENGTH
import com.log.vastgui.core.format.TableFormat
import com.log.vastgui.mars.base.MarsConfig
import com.log.vastgui.mars.base.MarsWriteMode
import com.tencent.mars.xlog.Log
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/1 0:12
// Documentation: https://ave.entropy2020.cn/documents/log/log-mars/usage/

/**
 * Mars Logger.
 *
 * @see MarsConfig
 * @since 1.3.4
 */
fun Logger.Companion.mars(
    logDir: File,
    logCache: File,
    logFormat: LogFormat = TableFormat(
        DEFAULT_MAX_SINGLE_LOG_LENGTH,
        DEFAULT_MAX_PRINT_TIMES,
        TableFormat.LogHeader.default
    ),
    mode: MarsWriteMode = MarsConfig.mode,
    namePreFix: String = MarsConfig.namePrefix,
    singleLogFileEveryday: Boolean = MarsConfig.singleLogFileEveryday,
    singleLogFileMaxSize: Long = MarsConfig.singleLogFileMaxSize,
    singleLogFileStoreTime: Long = MarsConfig.singleLogFileStoreTime,
    singleLogFileCacheDays: Int = MarsConfig.singleLogFileCacheDays,
    pubKey: String = MarsConfig.pubKey
) = MarsConfig.let {
    it.mode = mode
    it.logDir = logDir
    it.logCache = logCache
    it.namePrefix = namePreFix
    it.singleLogFileEveryday = singleLogFileEveryday
    it.singleLogFileMaxSize = singleLogFileMaxSize
    it.singleLogFileStoreTime = singleLogFileStoreTime
    it.singleLogFileCacheDays = singleLogFileCacheDays
    it.pubKey = pubKey
    MarsLogger.getInstance(logFormat)
}

/**
 * [Logger] based on [tencent/mars](https://github.com/Tencent/mars)
 * Implementation that integrates log printing and storage.
 *
 * @since 1.3.4
 */
class MarsLogger private constructor(override val logFormat: LogFormat) :
    Logger {
    override fun log(logInfo: LogInfo) {
        val content = logFormat.format(logInfo)
        when (logInfo.level) {
            LogLevel.VERBOSE -> Log.v(logInfo.tag, logInfo, content)
            LogLevel.DEBUG -> Log.d(logInfo.tag, logInfo, content)
            LogLevel.INFO -> Log.i(logInfo.tag, logInfo, content)
            LogLevel.WARN -> Log.w(logInfo.tag, logInfo, content)
            LogLevel.ERROR -> Log.e(logInfo.tag, logInfo, content)
            LogLevel.ASSERT -> Log.f(logInfo.tag, logInfo, content)
        }
    }

    /** @since 1.3.4 */
    fun close() {
        MarsConfig.close()
    }

    init {
        MarsConfig.init()
    }

    companion object : SingletonHolder<MarsLogger, LogFormat>(::MarsLogger)
}