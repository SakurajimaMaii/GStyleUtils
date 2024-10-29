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

package com.log.vastgui.slf4j

import com.log.vastgui.core.LogCat
import com.log.vastgui.slf4j.convert.convertLevel
import com.log.vastgui.slf4j.convert.convertMarker
import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.helpers.MessageFormatter
import org.slf4j.spi.CallerBoundaryAware
import org.slf4j.spi.LoggingEventBuilder
import java.util.function.Supplier

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/12
// Documentation: https://ave.entropy2020.cn/documents/log/log-slf4j/usage/

/**
 * Slf4j event builder.
 *
 * @since 1.3.10
 */
internal class Slf4jEventBuilder(
    private val logcat: LogCat,
    private val level: Level,
) : LoggingEventBuilder, CallerBoundaryAware {

    private val FQCN: String = Slf4jEventBuilder::class.java.getName()
    private var mFqcn: String? = FQCN
    private var mMessage: String? = null
    private var mCause: Throwable? = null
    private var mMarker: Marker? = null
    private val mArguments: MutableList<Any?> = mutableListOf()
    private var mKeyValuePairs: MutableMap<String, String>? = null

    override fun setCause(cause: Throwable?): LoggingEventBuilder = apply {
        this.mCause = cause
    }

    override fun addMarker(marker: Marker?): LoggingEventBuilder = apply {
        this.mMarker = marker
    }

    override fun addArgument(p: Any?): LoggingEventBuilder = apply {
        mArguments.add(p)
    }

    override fun addArgument(objectSupplier: Supplier<*>?): LoggingEventBuilder = apply {
        mArguments.add(objectSupplier?.get())
    }

    override fun addKeyValue(key: String?, value: Any?): LoggingEventBuilder {
        if (null == key || null == value) return this
        if (mKeyValuePairs == null) {
            mKeyValuePairs = HashMap()
        }
        mKeyValuePairs!![key] = value.toString()
        return this
    }

    override fun addKeyValue(key: String?, valueSupplier: Supplier<Any>?): LoggingEventBuilder {
        if (null == key || null == valueSupplier) return this
        if (mKeyValuePairs == null) {
            mKeyValuePairs = HashMap()
        }
        mKeyValuePairs!![key] = valueSupplier.get().toString()
        return this
    }

    override fun setMessage(message: String?): LoggingEventBuilder = apply {
        this.mMessage = message
    }


    override fun setMessage(messageSupplier: Supplier<String>?): LoggingEventBuilder = apply {
        this.mMessage = messageSupplier?.get()
    }

    override fun log() {
        val stackTrace = Throwable().stackTrace
        val index = stackTrace
            .indexOfLast { it.className == this::class.java.name && it.methodName == "log" }
            .coerceAtLeast(0)
        val level = convertLevel(level)
        val tag = convertMarker(mMarker, logcat.tag)
        val content = MessageFormatter.basicArrayFormat(mMessage, mArguments.toTypedArray())
        val kv = if (null == mKeyValuePairs) "" else "$mKeyValuePairs "
        val cause = if (null != mCause) mCause else MessageFormatter.getThrowableCandidate(mArguments.toTypedArray())
        logcat.log(level, tag, "$kv$content", cause, stackTrace[index + 1])
    }

    override fun log(message: String?) {
        setMessage(message).log()
    }

    override fun log(message: String?, arg: Any?) {
        setMessage(message).addArgument(arg).log()
    }

    override fun log(message: String?, arg0: Any?, arg1: Any?) {
        setMessage(message).addArgument(arg0).addArgument(arg1).log()
    }

    @Suppress("CheckResult")
    override fun log(message: String?, vararg args: Any?) {
        setMessage(message)
        for (arg in args) {
            addArgument(arg)
        }
        log()
    }

    override fun log(messageSupplier: Supplier<String>?) {
        setMessage(messageSupplier).log()
    }

    override fun setCallerBoundary(fqcn: String?) {
        this.mFqcn = fqcn
    }
}