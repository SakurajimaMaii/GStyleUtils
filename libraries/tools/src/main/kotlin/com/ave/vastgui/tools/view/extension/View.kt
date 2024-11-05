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

package com.ave.vastgui.tools.view.extension

import android.app.Service
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.constant.DEFAULT_LOG_TAG

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/3/30
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/extension/view/

// region Visibility
/** @since 1.2.1 */
fun View.gone() {
    isGone = true
}

/** @since 1.5.1 */
fun View.goneIfVisible() {
    if (isVisible) gone()
}

/** @since 1.5.1 */
fun View.goneIfInvisible() {
    if (isInvisible) gone()
}

/** @since 1.2.1 */
fun View.visible() {
    isVisible = true
}

/** @since 1.5.1 */
fun View.visibleIfGone() {
    if (isGone) visible()
}

/** @since 1.5.1 */
fun View.visibleIfInvisible() {
    if (isInvisible) visible()
}

/** @since 1.2.1 */
fun View.invisible() {
    isInvisible = true
}

/** @since 1.5.1 */
fun View.invisibleIfVisible() {
    if (isVisible) invisible()
}

/** @since 1.5.1 */
fun View.invisibleIfGone() {
    if (isGone) invisible()
}

// endregion

// region KeyBroad
/**
 * Show KeyBroad.
 *
 * @since 1.3.1
 */
fun EditText.showKeyBroad() {
    val imm = runCatching {
        cast<InputMethodManager>(context.getSystemService(Service.INPUT_METHOD_SERVICE))
    }.getOrElse {
        Log.e(DEFAULT_LOG_TAG, "Get InputMethodManager encounter exception:${it.stackTraceToString()}")
        null
    }
    requestFocus()
    imm?.showSoftInput(this, 0)
}

/**
 * Hide KeyBroad.
 *
 * @since 1.3.1
 */
fun EditText.hideKeyBroad() {
    val imm = runCatching {
        cast<InputMethodManager>(context.getSystemService(Service.INPUT_METHOD_SERVICE))
    }.getOrElse {
        Log.e(DEFAULT_LOG_TAG, "Get InputMethodManager encounter exception:${it.stackTraceToString()}")
        null
    }
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Return true if current [event] coordinate is outside the EditView, false
 * otherwise. Here is an example:
 * ```kotlin
 * // In Activity.kt
 * override fun onTouchEvent(event: MotionEvent?): Boolean {
 *     if (MotionEvent.ACTION_DOWN == event?.action) {
 *         val view = currentFocus
 *         if (null != view && view is EditText) {
 *             if (view.isShouldHideKeyBroad(event)) {
 *                 view.hideKeyBroad()
 *             }
 *         }
 *     }
 *     return super.onTouchEvent(event)
 * }
 * ```
 *
 * @since 1.3.1
 */
fun EditText.isShouldHideKeyBroad(event: MotionEvent): Boolean {
    val topLeft = intArrayOf(0, 0)
    getLocationOnScreen(topLeft)
    val left = topLeft[0]
    val top = topLeft[1]
    return !(event.rawX in left.toFloat()..(left + measuredWidth).toFloat() &&
            event.rawY in top.toFloat()..(top + measuredHeight).toFloat())
}

// endregion