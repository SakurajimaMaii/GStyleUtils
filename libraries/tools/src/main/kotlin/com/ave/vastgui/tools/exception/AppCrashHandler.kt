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

package com.ave.vastgui.tools.exception

import com.ave.vastgui.core.extension.nothing_to_do
import java.io.PrintWriter
import java.io.StringWriter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/25 1:54
// Description: App global exception handling.
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/exception/app-crash-handler/

/** @since 1.5.1 */
@FunctionalInterface
fun interface UncaughtExceptionHandler {
    fun dealAppCrash(t: Thread, e: Throwable, stackTraceInfo: String)
}

/**
 * App global exception handling.
 *
 * Here is a usage example:
 * ```kotlin
 * // App.kt
 * class App : Application() {
 *
 *     private val mLogger = ...
 *
 *     override fun onCreate() {
 *         super.onCreate()
 *         ...
 *         setDefaultUncaughtExceptionHandler { t, e, stackTraceInfo ->
 *             mLogger.e(stackTraceInfo)
 *         }
 *     }
 *
 * }
 * ```
 *
 * @since 1.4.1
 */
open class AppCrashHandler private constructor(): Thread.UncaughtExceptionHandler {
    final override fun uncaughtException(t: Thread, e: Throwable) {
        val sw = StringWriter()
        PrintWriter(sw, false).use {
            var cause = e.cause
            while (null != cause) {
                cause.printStackTrace(it)
                cause = cause.cause
            }
            it.flush()
        }
        uncaughtException(t, e, sw.toString())
    }

    open fun uncaughtException(thread: Thread, throwable: Throwable, stackTraceInfo: String) {
        nothing_to_do()
    }

    companion object {
        fun setDefaultUncaughtExceptionHandler(action: UncaughtExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(object : AppCrashHandler() {
                override fun uncaughtException(thread: Thread, throwable: Throwable, stackTraceInfo: String) {
                    action.dealAppCrash(thread, throwable, stackTraceInfo)
                }
            })
        }
    }
}