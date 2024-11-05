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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Description: Use SingletonHolder to build a singleton with parameters.
// Documentation: https://ave.entropy2020.cn/documents/core/extension/singleton-holder/
// Reference: https://juejin.cn/post/6844903775669321735

/**
 * Use SingletonHolder to build a singleton with parameters.
 *
 * ```kotlin
 * class Singleton private constructor(name: String) {
 *
 *     ... // do other things.
 *
 *     companion object:SingletonHolder<Singleton,String>(::Singleton)
 *
 * }
 *
 * // Get the singleton.
 * private val singleton by lazy {
 *     Singleton.getInstance(defaultLogTag())
 * }
 * ```
 *
 * @param T the singleton class.
 * @param A the parameters class.
 * @property creator the constructor of the T.
 * @since 0.0.3
 */
open class SingletonHolder<out T, in A>(private val creator: A.() -> T) {

    private var instance: T? = null

    fun getInstance(arg: A): T =
        instance ?: synchronized(this) {
            instance ?: creator(arg).apply { instance = this }
        }

}