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

import com.log.vastgui.core.LogCat

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5

/**
 * Log plugin
 *
 * @since 0.5.2
 */
interface LogPlugin<out TConfig, TPlugin> {

    val key: String

    /**
     * Configuring the plugin
     *
     * @return Plugins already configured.
     * @since 0.5.2
     */
    fun configuration(config: TConfig.() -> Unit): TPlugin

    /**
     * Install plugin to [scope]
     *
     * @param scope
     * @since 0.5.2
     */
    fun install(plugin: TPlugin, scope: LogCat)

}