/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.log.vastgui.okhttp

import com.ave.vastgui.core.extension.NotNullOrDefault
import com.ave.vastgui.core.extension.nothing_to_do
import com.log.vastgui.core.LogCat
import com.log.vastgui.core.annotation.LogExperimental
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogTag
import com.log.vastgui.okhttp.base.ContentLevel
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.promisesBody
import okhttp3.internal.sse.ServerSentEventReader
import okio.Buffer
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Locale
import java.util.concurrent.TimeUnit

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/24 0:01
// Description: Log interceptor of Okhttp3.
// Documentation: https://ave.entropy2020.cn/documents/log/log-okhttp/usage/
// Reference: https://square.github.io/okhttp/features/interceptors/

/**
 * Log interceptor of Okhttp3.
 *
 * ```kotlin
 * // Add Interceptor
 * val logcat = logFactory("global")
 * val okhttp = OkHttpClient
 *     .Builder()
 *     .addInterceptor(Okhttp3Interceptor(logcat))
 *     .build()
 *
 * // Make a request
 * val request: Request = Request.Builder()
 *     .url("http://127.0.0.1:7777")
 *     .build()
 * okhttp.newCall(request).execute()
 * ```
 *
 * @see <img
 * src=https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/log/okhttp/image/log.png?raw=true>
 * @since 1.3.3
 */
class Okhttp3Interceptor(private val logcat: LogCat) :
    Interceptor {

    /** @since 1.3.7 */
    private val sanitizedHeaders: MutableMap<String, String> = HashMap()

    /**
     * The filter function allows you to filter log messages for requests
     * matching the specified predicate. Return true directly by default.
     *
     * @since 1.3.3
     */
    var filter: ((Request) -> Boolean) by NotNullOrDefault { _ -> true }

    /**
     * Different levels represent different printing contents.
     *
     * @since 1.3.3
     */
    var contentLevel: ContentLevel by NotNullOrDefault(ContentLevel.ALL)

    /**
     * The [requestLevel] function allows you to set the log level it prints
     * based on [Request], and returns [LogLevel.DEBUG] by default.
     *
     * @since 1.3.3
     */
    var requestLevel: ((Request) -> LogLevel) by NotNullOrDefault { _ -> LogLevel.DEBUG }

    /**
     * The [responseLevel] function allows you to set the log level it prints
     * based on [Request], and returns [LogLevel.DEBUG] by default.
     *
     * @since 1.3.3
     */
    var responseLevel: ((Response) -> LogLevel) by NotNullOrDefault { _ -> LogLevel.DEBUG }

    /**
     * Request body json converter.
     *
     * @since 1.3.4
     */
    var bodyJsonConverter: ((String) -> String)? = null

    @OptIn(LogExperimental::class)
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        if (!filter(request) || ContentLevel.NONE == contentLevel) {
            return chain.proceed(request)
        }
        dealRequestLog(request, chain.connection())
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logcat.e(LogTag(logcat.tag), "<-- HTTP FAILED", e)
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        return dealResponseLog(response, tookMs)
    }

    /**
     * Deal request log.
     *
     * @since 1.3.3
     */
    @OptIn(LogExperimental::class)
    private fun dealRequestLog(request: Request, connection: Connection?) {
        val requestLog = StringBuilder()
        val requestBody = request.body
        val protocol = connection?.protocol() ?: Protocol.HTTP_1_1
        try {
            if (contentLevel.info) {
                requestLog.appendLine("--> ${request.method} ${request.url} $protocol")
            }
            if (contentLevel.headers) {
                val headers = request.headers
                request.headers.forEachIndexed { index, _ ->
                    val name = headers.name(index)
                    requestLog.appendLine("\t$name:${sanitizedHeaders[name] ?: headers.value(index)}")
                }
            }
            if (contentLevel.body && null != requestBody) {
                if (isPlaintext(requestBody.contentType())) {
                    requestLog.bodyToString(request)
                } else {
                    requestLog.appendLine("\tbody: maybe [binary body], omitted!")
                }
            }
        } catch (e: Exception) {
            logcat.e(
                LogTag(logcat.tag),
                "Exception encountered while processing request information",
                e
            )
        } finally {
            requestLog.append("--> END ${request.method}")
            logcat.log(requestLevel(request), LogTag(logcat.tag)(), requestLog.toString(), null, Throwable().stackTrace[0])
        }
    }

    /**
     * Deal response log.
     *
     * @since 1.3.3
     */
    @OptIn(LogExperimental::class)
    private fun dealResponseLog(response: Response, tookMs: Long): Response {
        val requestLog = StringBuilder()
        val builder = response.newBuilder()
        val clone: Response = builder.build()
        var responseBody = clone.body
        try {
            if (contentLevel.info) {
                requestLog
                    .appendLine("<-- ${clone.code} ${clone.message} ${clone.request.url} (${tookMs}ms)")
            }
            if (contentLevel.headers) {
                val headers = clone.headers
                headers.forEachIndexed { index, _ ->
                    requestLog.appendLine("\t ${headers.name(index)}:${headers.value(index)}")
                }
            }
            if (contentLevel.body && clone.promisesBody()) {
                if (responseBody == null) return response
                if (isEventStream(responseBody.contentType())) {
                    var isFirst = true
                    // https://stackoverflow.com/a/40002832/16905468
                    // https://stackoverflow.com/a/33862068/16905468
                    val source = responseBody.source()
                        .apply { request(Long.MAX_VALUE) }
                    val bufferClone = source.buffer.clone()
                    val body = bufferClone
                        .asResponseBody(responseBody.contentType(), responseBody.contentLength())
                    val reader =
                        ServerSentEventReader(source, object : ServerSentEventReader.Callback {
                            override fun onEvent(id: String?, type: String?, data: String) {
                                val json = bodyJsonConverter
                                    ?.invoke(data)
                                    ?.replace("\n", "\n\t      ")
                                val tab = if (isFirst) {
                                    isFirst = false; "body:"
                                } else "     "
                                requestLog.appendLine("\t $tab${json ?: data}")
                            }

                            override fun onRetryChange(timeMs: Long) {
                                nothing_to_do()
                            }
                        })
                    while (reader.processNextEvent()) {
                        nothing_to_do()
                    }

                    return response.newBuilder().body(body).build()
                }
                // Deal response as text
                else if (isPlaintext(responseBody.contentType())) {
                    val bufferSize = 1024 * 8
                    val bytes = ByteArrayOutputStream().use { output ->
                        responseBody!!.byteStream().copyTo(output, bufferSize)
                        output.toByteArray()
                    }
                    val contentType = responseBody.contentType()
                    val body = String(bytes, getCharset(contentType))
                    val json = bodyJsonConverter
                        ?.invoke(body)
                        ?.replace("\n", "\n\t      ")
                    requestLog.appendLine("\t body:${json ?: body}")
                    responseBody = bytes.toResponseBody(responseBody.contentType())
                    return response.newBuilder().body(responseBody).build()
                } else {
                    requestLog.appendLine("\tbody:maybe [binary body], omitted!")
                }
            }
        } catch (e: Exception) {
            logcat.e(
                LogTag(logcat.tag),
                "Exception encountered while processing response information",
                e
            )
        } finally {
            requestLog.append("<-- END HTTP")
            logcat.log(responseLevel(response), LogTag(logcat.tag)(), requestLog.toString(), null, Throwable().stackTrace[0])
        }
        return response
    }

    /**
     * Convert [Request.body] to string.
     *
     * @since 1.3.3
     */
    @OptIn(LogExperimental::class)
    private fun StringBuilder.bodyToString(request: Request) {
        try {
            val copy = request.newBuilder().build()
            val body = copy.body ?: return
            val buffer = Buffer()
            body.writeTo(buffer)
            val charset = getCharset(body.contentType())
            val bodyJson = buffer.readString(charset)
            val json = bodyJsonConverter
                ?.invoke(bodyJson)
                ?.replace("\n", "\n\t     ")
            appendLine("\tbody:${json ?: bodyJson}")
        } catch (e: Exception) {
            logcat.e(LogTag(logcat.tag), "Exception encountered while processing request body", e)
        }
    }

    /**
     * Allows you to sanitize sensitive headers to avoid their values appearing
     * in the logs. In the example below, Authorization header value will be
     * replaced with '***' when logging:
     *
     * ```kotlin
     * Okhttp3Interceptor(logcat)
     *      .sanitizedHeaders("Authorization","***")
     * ```
     *
     * @since 1.3.9
     */
    fun sanitizedHeaders(header: String, replaceWith: String) = apply {
        sanitizedHeaders[header] = replaceWith
    }

    companion object {
        private val UTF8: Charset = StandardCharsets.UTF_8

        /**
         * This method is only for compatibility with the way [Okhttp3Interceptor]
         * was created before version 1.3.5.
         */
        @Deprecated("Use constructor instead.", ReplaceWith("Okhttp3Interceptor(arg)"))
        fun getInstance(arg: LogCat): Okhttp3Interceptor = Okhttp3Interceptor(arg)

        /**
         * Get charset.
         *
         * @since 1.3.3
         */
        private fun getCharset(contentType: MediaType?): Charset {
            var charset = if (contentType != null) contentType.charset(UTF8) else UTF8
            if (charset == null) charset = UTF8
            return charset
        }

        /**
         * Returns true if the body in question probably contains human readable
         * text. Uses a small sample of code points to detect unicode control
         * characters commonly used in binary file signatures.
         *
         * @since 1.3.3
         */
        private fun isPlaintext(mediaType: MediaType?): Boolean {
            if (mediaType == null) return false
            if (mediaType.type == "text") {
                return true
            }
            var subtype = mediaType.subtype
            subtype = subtype.lowercase(Locale.getDefault())
            return subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html")
        }

        /** @since 1.3.5 */
        private fun isEventStream(mediaType: MediaType?): Boolean {
            return mediaType?.type == "text" && mediaType.subtype == "event-stream"
        }
    }
}