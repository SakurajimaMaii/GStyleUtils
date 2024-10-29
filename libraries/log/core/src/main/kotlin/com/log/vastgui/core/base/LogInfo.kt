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

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONWriter
import com.alibaba.fastjson2.annotation.JSONField
import com.log.vastgui.core.annotation.LogApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28

/**
 * Log information.
 *
 * @property threadName The name of the current thread.
 * @property stackTrace an array of stack trace elements representing the
 *     stack dump of LogInfo.
 * @property level The level of the log.
 * @property tag Used to identify the source of a log message. It usually
 *     identifies the class or activity where the log call occurs.
 * @property content The message you would like logged.
 * @property time The current time in milliseconds, only initialized when
 *     the object is created.
 * @since 0.5.2
 */
data class LogInfo @LogApi constructor(
    val threadName: String,
    @JSONField(serialize = false)
    val stackTrace: StackTraceElement?,
    val level: LogLevel,
    val tag: String,
    val time: Long,
    val content: String,
    val throwable: Throwable? = null
) {

    @JSONField(serialize = false)
    val traceLength = stackTrace.toString().length

    @JSONField(serialize = false)
    val printLength = traceLength.coerceAtLeast(content.length)

    @JSONField(serialize = false)
    val printBytesLength = (if (traceLength >= content.length) stackTrace.toString()
        .toByteArray().size else content.toByteArray().size).coerceAtLeast(50)

    @JSONField(serialize = false)
    val levelPriority: Int = level.priority

    /** @since 1.3.4 */
    val fileName = stackTrace?.fileName ?: ""

    /** @since 1.3.4 */
    val methodName = stackTrace?.methodName ?: ""

    /** @since 1.3.4 */
    val lineNumber = stackTrace?.lineNumber ?: 0

    /** @since 1.3.8 */
    val className = stackTrace?.className ?: "unknown class"

    override fun toString(): String = JSON.toJSONString(this, JSONWriter.Context(JSONWriter.Feature.PrettyFormat))

}