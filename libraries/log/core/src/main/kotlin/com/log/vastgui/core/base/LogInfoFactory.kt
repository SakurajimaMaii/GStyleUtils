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
import com.log.vastgui.core.internel.LazyMessageWrapper
import com.log.vastgui.core.plugin.LogJson
import com.log.vastgui.core.plugin.LogPretty

/**
 * [LogInfo] factory.
 *
 * @since 1.3.4
 */
class LogInfoFactory @LogApi constructor(
    internal val level: LogLevel,
    internal val tag: String,
    content: Any,
    internal val threadName: String,
    internal val stackTrace: StackTraceElement,
    internal val tr: Throwable? = null
) {
    private var logInfo: LogInfo? = null

    /**
     * [String] or lazy String (() -> String)
     *
     * @since 1.3.4
     */
    private var rawContentOrLazy: Any = content

    /** @since 1.3.4 */
    private val isFuncContent: Boolean
        get() = rawContentOrLazy is LazyMessageWrapper

    /** @since 1.3.4 */
    fun content(): Any {
        if (isFuncContent) {
            rawContentOrLazy = (rawContentOrLazy as LazyMessageWrapper).lazyMsg()
        }
        return rawContentOrLazy
    }

    /**
     * For json.
     *
     * @see LogJson
     * @since 1.3.4
     */
    fun setStringContent(content: String) {
        rawContentOrLazy = content
    }

    /**
     * For pretty.
     *
     * @see LogPretty
     * @since 1.3.4
     */
    fun getStringContent(): String {
        check(rawContentOrLazy is String) { "rawContentOrLazy should be String." }
        return rawContentOrLazy as String
    }

    /**
     * Get the [LogInfo] built by [LogInfoFactory], if [LogInfo] is not
     * initialized then build it first and assign it to [logInfo], otherwise
     * return [logInfo].
     *
     * @since 1.3.4
     */
    fun build(): LogInfo = logInfo ?: LogInfo(threadName, stackTrace, level, tag,
        System.currentTimeMillis(), content() as String, tr).also { logInfo = it }

}