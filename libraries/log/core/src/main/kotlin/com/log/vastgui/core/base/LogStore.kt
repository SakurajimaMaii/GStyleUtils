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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/13 23:34
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/develop/#logstore/

/**
 * Log store.
 *
 * @since 1.3.1
 */
interface LogStore {

    /**
     * Format [LogInfo] in the style specified by [LogFormat].
     *
     * @since 1.3.4
     */
    val logFormat: LogFormat
        get() = TODO("Please override logFormat!")

    /** @since 1.3.1 */
    fun store(logInfo: LogInfo)

    companion object
}