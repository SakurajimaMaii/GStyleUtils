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

package com.log.vastgui.slf4j

import com.log.vastgui.core.LogFactory
import com.log.vastgui.core.annotation.LogExperimental
import org.slf4j.ILoggerFactory
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/7
// Documentation: https://ave.entropy2020.cn/documents/log/log-slf4j/usage/

/**
 * [Slf4jFactory] Use [LogFactory] to create [Slf4jLogger].
 *
 * @since 1.3.7
 */
@LogExperimental
internal class Slf4jFactory(private val logFactory: LogFactory) : ILoggerFactory {

    /** @since 1.3.7 */
    private val logcatMap: ConcurrentMap<String, Logger> = ConcurrentHashMap()

    override fun getLogger(name: String): Logger {
        val oldValue: Logger? = logcatMap[name]
        if (null != oldValue) return oldValue
        return createLogger(name).also { logcatMap.putIfAbsent(name, it) }
    }

    /** @since 1.3.7 */
    private fun createLogger(name: String): Logger {
        return Slf4jLogger(logFactory(name))
    }

}