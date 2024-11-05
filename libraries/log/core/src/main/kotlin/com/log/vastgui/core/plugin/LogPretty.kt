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
import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.json.Converter
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/23 1:13
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/plugin/pretty/

/**
 * [LogPretty] allows printing the json string in the log in pretty style.
 *
 * ```kotlin
 * private val gsonConverter = GsonConverter.getInstance(true)
 *
 * @JvmField
 * val logFactory: LogFactory = getLogFactory {
 *     ....
 *     install(LogPretty) {
 *         converter = gsonConverter
 *     }
 * }
 * ```
 *
 * @since 1.3.4
 */
class LogPretty private constructor(mConfiguration: Configuration) {

    private val mConverter: Converter = mConfiguration.converter

    class Configuration internal constructor() {
        var converter: Converter by Delegates.notNull()
    }

    /** @since 1.3.8 */
    private fun parseString(json: String): Any = mConverter.parseString(json)

    companion object : LogPlugin<Configuration, LogPretty> {

        override val key: String = LogPretty::class.java.simpleName

        override fun install(plugin: LogPretty, scope: LogCat) {
            scope.logPipeline.intercept(LogPipeline.Render) {
                val rawContent = subject.getStringContent()
                // Try to render the log content and return the
                // original content if an exception occurs.
                val content = plugin.parseString(rawContent)
                subject.setStringContent(content.toString())
                proceedWith(subject)
            }
        }

        override fun configuration(config: Configuration.() -> Unit): LogPretty {
            val configuration = Configuration().also(config)
            return LogPretty(configuration)
        }
    }
}