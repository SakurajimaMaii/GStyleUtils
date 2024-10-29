/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ktor

import com.log.vastgui.okhttp.Okhttp3Interceptor
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.test.runTest
import logcat
import org.junit.Test
import java.io.File

class KtorTest {

    @Test
    fun post() = runTest {
        client.post("http://127.0.0.1:7777/post") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    companion object {
        val client = HttpClient(OkHttp) {
            engine {
                addInterceptor(Okhttp3Interceptor(logcat)
                    .sanitizedHeaders("Authorization","***")
                    .sanitizedHeaders("User-Agent","xxx")
                )
            }
        }
    }

}