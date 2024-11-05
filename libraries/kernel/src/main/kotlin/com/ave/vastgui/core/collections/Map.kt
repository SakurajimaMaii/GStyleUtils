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

@file:JvmName("MapKt")

package com.ave.vastgui.core.collections

import kotlin.reflect.full.memberProperties

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/28
// Documentation: https://ave.entropy2020.cn/documents/core/collections/map/

/**
 * Return object as a map, where key is the parameter name and value is the
 * parameter value.
 *
 * @since 0.0.4
 */
fun Any.toMap() = this::class
    .memberProperties
    .associate {
        it.name to it.call(this)
    }