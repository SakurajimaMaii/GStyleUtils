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

package com.devenv.vastgui.streamapp.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import javax.print.attribute.standard.RequestingUserName

@RestController
class IndexController {
    class User(val name: String, val age: Int)

    @GetMapping("/")
    suspend fun index() =
        "{\"name\":\"BeJson\",\"url\":\"http://www.bejson.com\",\"page\":88,\"isNonProfit\":true}"

    @GetMapping("/content")
    suspend fun content() = run {
        val file = File(javaClass.classLoader!!.getResource("paragraph.txt").file)
        val content = String(file.readBytes())
        "{\"image\":\"$content\"}"
    }

    @PostMapping("/post")
    fun post() = User("小李", 19)

    @GetMapping("stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun stream() = flow {
        repeat(3) {
            emit("{\"name\":\"BeJson\",\"url\":\"http://www.bejson.com\",\"page\":88,\"isNonProfit\":true}")
            delay(50)
        }
    }

    @GetMapping("multi", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun multi() = flow {
        emit("{")
        delay(50)
        emit("\"name\": \"zhangsan\",")
        delay(50)
        emit("\"age\", 25")
        delay(50)
        emit("}")
    }


}