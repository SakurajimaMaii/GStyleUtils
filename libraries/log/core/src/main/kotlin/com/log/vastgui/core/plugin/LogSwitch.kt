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
import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.plugin.LogSwitch.Configuration

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/plugin/switch/

/**
 * [LogSwitch] is used to control whether to enable logging.
 *
 * Before the **1.3.8** version, if the user does not install [LogSwitch],
 * the log will be printed by default. From **1.3.8** Starting from
 * version, users can only enable logs if they have installed [LogSwitch]
 * and set [LogSwitch.open] to `true` .
 *
 * @see LogStateChecker
 * @since 0.5.2
 */
class LogSwitch internal constructor(
    val open: Boolean
) {

    /**
     * Configuration of [LogSwitch].
     *
     * @property open [LogCat] will print log when the [open] is true and the
     * app is in debug mode.
     * @since 0.5.4
     */
    class Configuration {
        var open: Boolean = true
    }

    companion object : LogPlugin<Configuration, LogSwitch> {

        override val key: String
            get() = LogSwitch::class.java.simpleName

        override fun configuration(config: Configuration.() -> Unit): LogSwitch {
            val configuration = Configuration().also(config)
            return LogSwitch(configuration.open)
        }

        override fun install(plugin: LogSwitch, scope: LogCat) {
            scope.logEnabled = plugin.open
        }

    }

}