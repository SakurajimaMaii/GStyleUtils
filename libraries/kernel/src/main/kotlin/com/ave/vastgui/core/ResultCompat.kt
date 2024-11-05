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

package com.ave.vastgui.core

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/31

/**
 * ResultCompat.
 *
 * It is intended to solve the problem of being unable to obtain
 * [kotlin.Result] in java.
 *
 * @since 0.0.7
 */
class ResultCompat<T>(val result: Result<T>) {

    companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful
         * value.
         *
         * @since 0.0.7
         */
        fun <T> success(value: T): ResultCompat<T> = ResultCompat(Result.success(value))

        /**
         * Returns an instance that encapsulates the given [Throwable] as failure.
         *
         * @since 0.0.7
         */
        fun <T> failure(throwable: Throwable): ResultCompat<T> =
            ResultCompat(Result.failure(throwable))
    }

    /**
     * Returns `true` if [result] instance represents a successful outcome. In
     * this case [ResultCompat.isFailure] return `false` .
     *
     * @since 0.0.7
     */
    val isSuccess: Boolean
        get() = result.isSuccess

    /**
     * Returns `true` if [result] instance represents a failed outcome. In this
     * case [ResultCompat.isSuccess] returns `false`.
     *
     * @since 0.0.7
     */
    val isFailure: Boolean
        get() = result.isFailure

    /** @see Result.getOrNull */
    fun getOrNull(): T? = result.getOrNull()

    /** @see Result.exceptionOrNull */
    fun exceptionOrNull(): Throwable? = result.exceptionOrNull()

    override fun toString(): String =
        if (isSuccess) "ResultCompat(value = ${getOrNull()})"
        else "ResultCompat(error = ${exceptionOrNull()?.message})"
}
