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
import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.json.Converter
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/8
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/plugin/json/

/**
 * The [LogJson] plugin allows you to convert objects to json.
 *
 * ```json
 * private val gsonConverter = GsonConverter.getInstance(true)
 *
 * val logFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogJson) {
 *         converter = gsonConverter
 *     }
 * }
 * ```
 *
 * @since 0.5.3
 */
class LogJson private constructor(mConfiguration: Configuration) {

    private val mConverter: Converter = mConfiguration.converter

    /**
     * Configuration of [LogJson].
     *
     * @property converter The json converter.
     * @since 0.5.3
     */
    class Configuration internal constructor() {
        var converter: Converter by Delegates.notNull()
    }

    /** @since 1.3.8 */
    private fun toJson(data: Any): String = mConverter.toJson(data)

    companion object : LogPlugin<Configuration, LogJson> {

        override val key: String = LogJson::class.java.simpleName

        override fun install(plugin: LogJson, scope: LogCat) {
            scope.logPipeline.intercept(LogPipeline.Transform) {
                val content = subject.content()
                if (content is String) {
                    proceed()
                    return@intercept
                }
                val json = plugin.toJson(content)
                subject.setStringContent(json)
                proceedWith(subject)
            }
        }

        override fun configuration(config: Configuration.() -> Unit): LogJson {
            val configuration = Configuration().also(config)
            return LogJson(configuration)
        }
    }
}