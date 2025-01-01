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

package com.ave.vastgui.tools.content

import android.app.Application
import android.content.ContentResolver
import android.content.Context

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/6/20
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/context-helper/

/** Help utils to get context. */
object ContextHelper {

    private lateinit var application: Application

    /**
     * Initialize [ContextHelper], please call [ToolsConfig.init]
     *
     * @param application the application of your app.
     */
    @JvmStatic
    internal fun init(application: Application) {
        if (!ContextHelper::application.isInitialized) {
            ContextHelper.application = application
        }
    }

    /**
     * Get application of the app.
     *
     * @return application object.
     */
    @JvmStatic
    fun getApp(): Application =
        if (!ContextHelper::application.isInitialized) {
            throw UninitializedPropertyAccessException("application is not init, please call ToolsConfig.init()")
        } else application

    /**
     * Get applicationContext of the app.
     *
     * @return applicationContext.
     */
    @JvmStatic
    fun getAppContext(): Context =
        if (!ContextHelper::application.isInitialized) {
            throw UninitializedPropertyAccessException("application is not init, please call ToolsConfig.init()")
        } else application.applicationContext

    /**
     * Get [ContentResolver] by [getAppContext]
     *
     * @since 1.5.2
     */
    fun getContentResolver(): ContentResolver = getAppContext().contentResolver

}
