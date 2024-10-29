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

@file:JvmName("LogFactoryKt")

package com.log.vastgui.core

import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.plugin.LogStateChecker
import com.log.vastgui.core.plugin.LogTypeValidator

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/setting-up/

/**
 * Get log factory
 *
 * @since 0.5.2
 */
fun getLogFactory(factory: LogFactory.() -> Unit): LogFactory = LogFactory().also(factory)

/**
 * Log factory. You can only get a [LogFactory] by [getLogFactory]. By
 * default, you should only create one factory in your app.
 *
 * @since 0.5.2
 */
class LogFactory internal constructor() {

    private val plugins: MutableMap<String, (LogCat) -> Unit> = mutableMapOf()
    private val pluginConfigurations: MutableMap<String, Any.() -> Unit> = mutableMapOf()

    init {
        // install default plugin
        install(LogTypeValidator)
        install(LogStateChecker)
    }

    /**
     * Install plugin for your log.
     *
     * @since 0.5.2
     */
    @Suppress("UNCHECKED_CAST")
    fun <TConfig : Any, TPlugin : Any> install(
        plugin: LogPlugin<TConfig, TPlugin>,
        configure: TConfig.() -> Unit = {}
    ) {
        val previousConfigBlock = pluginConfigurations[plugin.key]
        pluginConfigurations[plugin.key] = {
            previousConfigBlock?.invoke(this)
            (this as TConfig).configure()
        }

        if (plugins.containsKey(plugin.key)) return

        plugins[plugin.key] = { scope ->
            // FIX: https://github.com/SakurajimaMaii/Android-Vast-Extension/issues/148
            val config: (Any.() -> Unit) = pluginConfigurations[plugin.key]!!
            val pluginData: TPlugin = plugin.configuration(config)
            plugin.install(pluginData, scope)
        }
    }

    /**
     * Get log with [clazz].
     *
     * @since 1.3.4
     */
    @Deprecated(
        "The clazz parameter is passed in just to get the class name as the default log " +
                "tag, but this may cause misunderstanding for some users, so this API is marked as " +
                "deprecated at the WARNING level.",
        ReplaceWith("invoke(clazz.simpleName)"),
        DeprecationLevel.WARNING
    )
    fun getLogCat(clazz: Class<*>) =
        LogCat(clazz.simpleName).apply(::install)

    /**
     * Get log with [tag].
     *
     * @since 1.3.4
     */
    @Deprecated("Use invoke instead.", ReplaceWith("invoke(tag)"))
    fun getLogCat(tag: String = ""): LogCat =
        LogCat(tag).apply(::install)

    /**
     * Install the plugin to [logcat].
     *
     * @since 0.5.2
     */
    private fun install(logcat: LogCat) {
        plugins.values.forEach { logcat.apply(it) }
    }

    /**
     * ```kt
     * val logFactory: LogFactory = getLogFactory {
     *     ....
     * }
     *
     * val logcat: LogCat = logFactory("OpenApi")
     * ```
     *
     * @since 1.3.5
     */
    operator fun invoke(tag: String = ""): LogCat = getLogCat(tag)

    /**
     * Use the class name of [clazz] as the tag of the log.
     *
     * ```kt
     * val logFactory: LogFactory = getLogFactory {
     *     ....
     * }
     *
     * val logcat: LogCat = logFactory(OpenApi::class.java)
     * ```
     *
     * @since 1.3.5
     */
    @Deprecated(
        "The clazz parameter is passed in just to get the class name as the default log " +
                "tag, but this may cause misunderstanding for some users, so this API is marked as " +
                "deprecated at the WARNING level.",
        ReplaceWith("invoke(clazz.simpleName)"),
        DeprecationLevel.WARNING
    )
    operator fun invoke(clazz: Class<*>): LogCat = getLogCat(clazz.simpleName)

}