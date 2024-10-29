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
import com.log.vastgui.core.base.LogInfoFactory
import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.pipeline.PipelinePhase

// Author: ywnkm
// Email: https://github.com/ywnkm
// Date: 2024/6/22

/**
 * If the log content is an object, [LogTypeValidator] will check whether
 * the object has been correctly converted to a string.
 *
 * @see LogJson
 * @since 1.3.4
 */
class LogTypeValidator internal constructor() {

    companion object : LogPlugin<Unit, LogTypeValidator> {

        private val instance = LogTypeValidator()

        override val key: String = LogTypeValidator::class.java.simpleName

        override fun configuration(config: Unit.() -> Unit): LogTypeValidator {
            return instance
        }

        override fun install(plugin: LogTypeValidator, scope: LogCat) {
            val afterTransform = PipelinePhase("AfterTransform")
            scope.logPipeline.insertPhaseAfter(LogPipeline.Transform, afterTransform)
            scope.logPipeline.intercept(afterTransform) {
                if (subject.content() !is String) {
                    val message =
                        "Can not convert ${subject.content().javaClass}, please install a specific converter plugin."
                    // Because log printing will be affected by the configured level,
                    // the original log level is retained here.
                    val threadName = Thread.currentThread().name
                    val trace = Throwable().stackTrace[13]
                    val builder = LogInfoFactory(subject.level, subject.tag, message, threadName, trace)
                    proceedWith(builder)
                } else {
                    proceed()
                }
            }
        }
    }
}