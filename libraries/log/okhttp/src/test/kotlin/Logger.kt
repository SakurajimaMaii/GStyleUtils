/*
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
import com.log.vastgui.core.format.TableFormat
import com.log.vastgui.core.getLogFactory
import com.log.vastgui.core.json.GsonConverter
import com.log.vastgui.core.plugin.LogJson
import com.log.vastgui.core.plugin.LogPrinter
import com.log.vastgui.core.plugin.LogSwitch
import com.log.vastgui.okhttp.Okhttp3Interceptor
import okhttp3.OkHttpClient

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/9/29

val gson = GsonConverter.getInstance(true)
val logFactory = getLogFactory {
    install(LogSwitch) {
        open = true
    }
    install(LogPrinter) {
        logger = object : Logger {
            override val logFormat: LogFormat
                get() = TableFormat(30, 10, TableFormat.LogHeader.default)

            override fun log(logInfo: LogInfo) {
                println(logFormat.format(logInfo))
            }
        }
    }
    install(LogJson) {
        converter = gson
    }
}
val logcat = logFactory("global")
val okhttp = OkHttpClient
    .Builder()
    .addInterceptor(Okhttp3Interceptor(logcat).apply {
        bodyJsonConverter = { gson.parseString(it) }
    })
    .build()