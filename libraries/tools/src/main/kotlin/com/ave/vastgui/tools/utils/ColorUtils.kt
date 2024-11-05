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

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import kotlin.math.roundToInt

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/app-resources/color-utils/

object ColorUtils {

    /** Color hex regex.Supported formats are `#RRGGBB` and `#AARRGGBB`. */
    const val COLOR_HEX_REGEX = "^#([A-Fa-f\\d]{6}|[A-Fa-f\\d]{8})$"

    /**
     * Color hex regex pattern.
     *
     * @since 1.5.1
     */
    val COLOR_HEX_PATTERN = Regex(COLOR_HEX_REGEX)

    /**
     * Map of color opacity.
     *
     * @since 1.5.1
     */
    @JvmField
    val ColorOpacity: Map<Int, String> = buildMap(101) {
        for (i in 0..100) {
            put(i, ((i / 100f) * 255f).roundToInt().toString(16).padStart(2, '0').uppercase())
        }
    }

    /** Map of color transparency. */
    @JvmField
    val ColorTransparency = ColorOpacity.mapKeys { 100 - it.key }

    /**
     * Parse the color string, and return the corresponding color-int.
     *
     * @return The color-int of the [colorHex] , [default] otherwise.
     * @throws IllegalArgumentException
     * @see Color.parseColor
     * @since 1.5.1
     */
    @JvmStatic
    fun colorHex2Int(colorHex: String, default: String = "#00000000"): Int =
        if (isColorHex(colorHex)) Color.parseColor(colorHex)
        else if (isColorHex(default)) Color.parseColor(default)
        else throw IllegalArgumentException("$colorHex $default are all illegal color values.")

    /**
     * Converting color hexadecimal string to an array of ARGB.
     *
     * @param colorHex Color string like **#12c2e9** .
     * @return The color argb array like **{255,63,226,197}** , [default]
     * otherwise.
     * @throws IllegalArgumentException
     * @since 1.5.1
     */
    @JvmStatic
    fun colorHex2ARGB(colorHex: String, default: String = "#00000000"): IntArray {
        val colorInt = colorHex2Int(colorHex, default)
        return colorInt2ARGB(colorInt)
    }

    /**
     * Converting color-int to string.
     *
     * @param colorInt color-int.
     * @return color string like **#3FE2C5** .
     * @throws IllegalArgumentException
     * @since 1.5.1
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun colorInt2Hex(@ColorInt colorInt: Int, @ColorInt default: Int = Color.TRANSPARENT): String {
        val hex = fun(value: Int) = value.toString(16).padStart(2, '0')
        return if (colorInt.toUInt() in 0u..0xFFFFFFFFu) {
            "#${hex(Color.alpha(colorInt))}${hex(Color.red(colorInt))}${hex(Color.green(colorInt))}${hex(Color.blue(colorInt))}"
                .uppercase()
        } else if (default.toUInt() in 0u..0xFFFFFFFFu) {
            "#${hex(Color.alpha(default))}${hex(Color.red(default))}${hex(Color.green(default))}${hex(Color.blue(default))}"
                .uppercase()
        } else {
            throw IllegalArgumentException("$colorInt(hex=${colorInt.toString(16)}) $default(hex=${default.toString(16)}) are all illegal color values.")
        }
    }

    /**
     * Converting color-int to an array of ARGB.
     *
     * @param colorInt color-int.
     * @return An array of ARGB, like **{255,63,226,197}** .
     * @throws IllegalArgumentException
     * @since 1.5.1
     */
    @JvmStatic
    fun colorInt2ARGB(@ColorInt colorInt: Int, @ColorInt default: Int = Color.TRANSPARENT): IntArray {
        return if (colorInt.toUInt() in 0u..0xFFFFFFFFu) {
            intArrayOf(Color.alpha(colorInt), Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt))
        } else if (default.toUInt() in 0u..0xFFFFFFFFu) {
            intArrayOf(Color.alpha(default), Color.red(default), Color.green(default), Color.blue(default))
        } else {
            throw IllegalArgumentException("$colorInt(hex=${colorInt.toString(16)}) $default(hex=${default.toString(16)}) are all illegal color values.")
        }
    }

    /**
     * Converting an argb array to color string.
     *
     * @param argb an argb array like **{63,226,197}** .
     * @return Color hexadecimal string like **#3FE2C5** .
     */
    @JvmStatic
    fun colorRGB2Hex(argb: IntArray): String {
        var hexCode = "#"
        for (value in argb.map { it.coerceIn(0, 255) }) {
            val lCode = (value and 0x0F).toString(16)
            val hCode = (value shr 4).toString(16)
            hexCode += hCode + lCode
        }
        return hexCode.uppercase()
    }

    /**
     * Get the color hex in the format of **#AARRGGBB** by [transparency] and
     * [colorInt].
     *
     * If the given [colorInt] itself has transparency, it will be forced to
     * the transparency specified by [transparency].
     *
     * @throws IllegalArgumentException
     */
    @JvmStatic
    fun getColorWithTransparency(
        @IntRange(from = 0, to = 100) transparency: Int,
        colorInt: Int
    ): String {
        if (colorInt.toUInt() in 0u..0xFFFFFFFFu)
            throw IllegalArgumentException("$colorInt(hex=${colorInt.toString(16)}) is illegal color values.")
        val color = Color.rgb(Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt))
        val colorHex = colorInt2Hex(color)
        return StringBuilder(colorHex).replace(1, 3, ColorTransparency[transparency.coerceIn(0, 100)]!!)
            .toString().uppercase()
    }

    /**
     * Get the color-int by [transparency] and [colorInt].
     *
     * If the given [colorInt] itself has transparency, it will be forced to
     * the transparency specified by [transparency].
     *
     * @throws IllegalArgumentException
     * @since 0.5.3
     */
    fun getColorIntWithTransparency(
        @IntRange(from = 0, to = 100) transparency: Int,
        colorInt: Int
    ) = colorHex2Int(getColorWithTransparency(transparency, colorInt))

    /**
     * Return true if the color hex string is right,false otherwise.
     *
     * @param colorHex color hex string.
     * @return true if the color hex string is right,false otherwise.
     * @since 1.5.1
     */
    @JvmStatic
    fun isColorHex(vararg colorHex: String): Boolean {
        return colorHex.all { COLOR_HEX_PATTERN.matches(it) }
    }

}