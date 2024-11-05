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

@file:JvmName("StringsKt")

package com.ave.vastgui.core.text

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/4/27
// Description: String conversions.
// Documentation: https://ave.entropy2020.cn/documents/core/text/string-number-conversions/

/**
 * Parses the string as an [Int] number and returns the result.
 *
 * @param defaultValue Return if the string is not a valid representation
 *     of a number.
 * @param radix Radix for string to number conversion.
 * @since 0.1.0
 */
@JvmOverloads
fun String.safeToInt(defaultValue: Int, radix: Int = 10) = toIntOrNull(radix) ?: defaultValue

/**
 * Parses the string as an [Long] number and returns the result.
 *
 * @param defaultValue Return if the string is not a valid representation
 *     of a number.
 * @param radix Radix for string to number conversion.
 * @since 0.1.0
 */
@JvmOverloads
fun String.safeToLong(defaultValue: Long, radix: Int = 10) = toLongOrNull(radix) ?: defaultValue

/**
 * Parses the string as an [Float] number and returns the result.
 *
 * @param defaultValue Return if the string is not a valid representation
 *     of a number.
 * @since 0.1.0
 */
fun String.safeToFloat(defaultValue: Float) =
    toFloatOrNull() ?: defaultValue

/**
 * Parses the string as an [Double] number and returns the result.
 *
 * @param defaultValue Return if the string is not a valid representation
 *     of a number.
 * @since 0.1.0
 */
fun String.safeToDouble(defaultValue: Double) =
    toDoubleOrNull() ?: defaultValue

/**
 * Parses the string as a [java.math.BigInteger] number and returns the
 * result.
 *
 * @param defaultValue Return if the string is not a valid representation
 *     of a number.
 * @param radix Radix for string to number conversion.
 * @since 0.1.0
 */
@JvmOverloads
fun String.safeToBigInteger(defaultValue: BigInteger, radix: Int = 10) =
    toBigIntegerOrNull(radix) ?: defaultValue

/**
 * Parses the string as a [java.math.BigDecimal] number and returns the
 * result.
 *
 * @param defaultValue Return if the string is not a valid representation
 *     of a number.
 * @param mathContext Specifies the precision and the rounding mode.
 * @since 0.1.0
 */
@JvmOverloads
fun String.safeToBigDecimal(defaultValue: BigDecimal, mathContext: MathContext? = null) =
    mathContext?.apply { toBigDecimalOrNull(this) } ?: toBigDecimalOrNull() ?: defaultValue