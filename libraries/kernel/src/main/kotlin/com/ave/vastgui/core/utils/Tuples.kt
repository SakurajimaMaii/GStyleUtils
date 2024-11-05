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

@file:JvmName("TuplesKt")

package com.ave.vastgui.core.utils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/28
// Documentation: https://ave.entropy2020.cn/documents/core/utils/tuples/

/**
 * Represents a quadruple of values.
 *
 * @since 0.0.4
 */
data class Quadruple<out A, out B, out C, out D>(
    val param1: A,
    val param2: B,
    val param3: C,
    val param4: D
) {
    override fun toString() = "($param1,$param2,$param3,$param4)"
}

/**
 * Represents a quintuple of values.
 *
 * @since 0.0.4
 */
data class Quintuple<out A, out B, out C, out D, out E>(
    val param1: A,
    val param2: B,
    val param3: C,
    val param4: D,
    val param5: E
) {
    override fun toString() = "($param1,$param2,$param3,$param4$param4)"
}