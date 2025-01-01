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

package com.ave.vastgui.tools.utils

import android.os.Build
import android.provider.Settings
import com.ave.vastgui.tools.content.ContextHelper
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Help you to get the system information.
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/information-get/system-utils/

object SystemUtils {
    /**
     * Returns the language code of this Locale.
     *
     * For example: the current setting is "Chinese-China", then "zh" will be
     * returned.
     *
     * @return the language code of this Locale.
     */
    @JvmStatic
    val systemLanguage: String
        get() = Locale.getDefault().language

    /**
     * Returns an array of all installed locales.
     *
     * @return an array of installed locales.
     */
    @JvmStatic
    val systemLanguageList: Array<out Locale>
        get() = Locale.getAvailableLocales()

    /**
     * Get the current mobile phone android version.
     *
     * @return for example: "11" will be returned.
     */
    @JvmStatic
    val systemAndroidVersion: String
        get() = Build.VERSION.RELEASE

    /** @return the end-user-visible name for the end product. */
    @JvmStatic
    val systemModel: String
        get() = Build.MODEL

    /**
     * @return the consumer-visible brand with which the product/hardware will
     * be associated, if any.
     */
    @JvmStatic
    val deviceBrand: String
        get() = Build.BRAND
}

/**
 * Whether Airplane Mode is on.
 *
 * @since 1.5.2
 */
fun isAirplaneMode(): Boolean {
    return Settings.System
        .getInt(ContextHelper.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1
}