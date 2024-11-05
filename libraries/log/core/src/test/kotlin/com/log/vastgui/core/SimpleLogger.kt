package com.log.vastgui.core/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.Logger
import com.log.vastgui.core.format.DEFAULT_MAX_PRINT_TIMES
import com.log.vastgui.core.format.DEFAULT_MAX_SINGLE_LOG_LENGTH
import com.log.vastgui.core.format.TableFormat

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/19 23:32
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/develop/

class SimpleLogger(
    override val logFormat: LogFormat = TableFormat(
        DEFAULT_MAX_SINGLE_LOG_LENGTH,
        DEFAULT_MAX_PRINT_TIMES,
        TableFormat.LogHeader.default
    )
) : Logger {
    override fun log(logInfo: LogInfo) {
        println(logFormat.format(logInfo))
    }
}