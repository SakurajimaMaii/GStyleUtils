/*
 * Copyright 2022 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.app

import android.app.Application
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.app.log.marsLogger
import com.ave.vastgui.tools.exception.AppCrashHandler.Companion.setDefaultUncaughtExceptionHandler
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle
import com.log.vastgui.android.lifecycle.LifecycleLogcat.Companion.registerLifecycleLogcat
import com.log.vastgui.core.annotation.LogExperimental
import com.log.vastgui.core.base.LogTag

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/12/28

class App : Application() {

    private val mLogcat by lazy { logFactory("App") }

    @OptIn(LogExperimental::class)
    override fun onCreate() {
        super.onCreate()
        DialogX.dialogMaxWidth = 400f.DP.toInt()
        DialogX.globalStyle = IOSStyle()
        DialogX.init(this)
        registerLifecycleLogcat { tag, event, bundle ->
            mLogcat.i(LogTag(tag), "$event $bundle")
            // Log.i(tag, "$event $bundle")
        }
//        WindowManager.getInstance().init(this, OptionFactory())
//        ThemeSkinService.getInstance().apply {
//            createViewInterceptor.add(FabFactory())
//            addThemeSkinExecutorBuilder(FabExecutorBuilder())
//        }
//         ConstraintLayoutCompat.init()
        setDefaultUncaughtExceptionHandler { _, _, stackTraceInfo ->
            mLogcat.e(stackTraceInfo)
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        marsLogger.close()
    }

    init {
//        Config.getInstance().skinMode = Config.SkinMode.REPLACE_ALL
//        Config.getInstance().isEnableDebugMode = true
//        Config.getInstance().isEnableStrictMode = true
//        Config.getInstance().performanceMode = Config.PerformanceMode.EXPERIENCE_FIRST
    }

}