/*
 * Copyright 2024 VastGui
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

package com.ave.vastgui.core.extension

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/15
// Documentation: https://ave.entropy2020.cn/documents/core/extension/not-null-variable/

/**
 * Getting a non-null variable.
 *
 * @param T The variable type.
 * @param once If true, the variable will only set by once, false
 *     otherwise.
 * @since 0.0.1
 */
class NotNUllVar<T : Any> @JvmOverloads constructor(private val once: Boolean = false) :
    ReadWriteProperty<Any, T> {

    private var value: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value
            ?: throw UninitializedPropertyAccessException("${property.name} is not initialized.")
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        when {
            (null == this.value || (null != this.value && !once)) -> this.value = value
            (once) -> throw IllegalStateException("${property.name} has already been initialized.")
        }
    }

}

/**
 * Getting a non-null variable. If no other value is set, the
 * [defaultValue] will be used as the value of the variable and no changes
 * are allowed.
 *
 * @since 0.0.6
 */
class NotNullOrDefault<T : Any>(private val defaultValue: T) : ReadWriteProperty<Any, T> {

    private var mValue: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (mValue == null)
            mValue = defaultValue
        return mValue!!
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (mValue == null)
            mValue = value
        else throw RuntimeException("${property.name} has already been initialized.")
    }

}