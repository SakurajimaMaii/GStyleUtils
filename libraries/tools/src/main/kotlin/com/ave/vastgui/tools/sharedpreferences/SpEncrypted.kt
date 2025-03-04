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

package com.ave.vastgui.tools.sharedpreferences

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.ave.vastgui.core.extension.SingletonHolder
import com.ave.vastgui.tools.config.ToolsConfig
import com.ave.vastgui.tools.content.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/app-data-and-files/save-key-value-data/sp-encrypted/

/**
 * Creating an instance of encrypted SharedPreferences.
 *
 * ```kotlin
 * val sp = SpEncrypted(name).getSharedPreferences()
 * ```
 *
 * @param name The name of the file to open; can not contain path
 *     separators.
 */
class SpEncrypted(name: String) {

    private val mSharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            ContextHelper.getAppContext(),
            name,
            ToolsConfig.getMasterKey(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getSharedPreferences(): SharedPreferences {
        return mSharedPreferences
    }

    @Deprecated(
        level = DeprecationLevel.HIDDEN,
        message = "Users should be able to create multiple SpEncrypted instances."
    )
    companion object : SingletonHolder<SpEncrypted, String>(::SpEncrypted)

}