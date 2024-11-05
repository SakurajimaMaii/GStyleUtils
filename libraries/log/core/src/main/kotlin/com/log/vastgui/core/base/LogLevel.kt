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

import com.log.vastgui.core.base.LogLevel.ASSERT
import com.log.vastgui.core.base.LogLevel.DEBUG
import com.log.vastgui.core.base.LogLevel.ERROR
import com.log.vastgui.core.base.LogLevel.INFO
import com.log.vastgui.core.base.LogLevel.VERBOSE
import com.log.vastgui.core.base.LogLevel.WARN

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/29

/** @since 1.3.4 */
val allLogLevel = setOf(VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT)

/**
 * Log level.
 *
 * @since 0.5.3
 */
sealed class LogLevel(val priority: Int) : Comparable<LogLevel> {
    object VERBOSE : LogLevel(2)
    object DEBUG : LogLevel(3)
    object INFO : LogLevel(4)
    object WARN : LogLevel(5)
    object ERROR : LogLevel(6)
    object ASSERT : LogLevel(7)

    override fun compareTo(other: LogLevel): Int = this.priority - other.priority

    override fun toString(): String = this::class.java.simpleName
}