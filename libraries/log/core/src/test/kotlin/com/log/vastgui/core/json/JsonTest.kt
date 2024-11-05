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

package com.log.vastgui.core.json

import com.alibaba.fastjson2.JSON
import com.google.gson.JsonParser
import com.google.gson.Strictness
import com.google.gson.stream.JsonReader
import org.junit.Test
import java.io.StringReader

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/12

class JsonTest {

    @Test
    fun gsonUsage() {
        try {
            val reader = JsonReader(StringReader("123456null#")).apply {
                strictness = Strictness.LENIENT
            }
            println(JsonParser.parseReader(reader)::class.java.simpleName)
            println(JsonParser.parseString("123456null#")::class.java.simpleName)
            //println(JsonParser.parseString("#123456")::class.java.simpleName)
            //println(JsonParser.parseString("123456")::class.java.simpleName)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    @Test
    fun fastJsonUsage() {
        try {
            println(JSON.isValid("null#123456"))
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    @Test
    fun jacksonUsage() {
        try {
            //println(JsonParser.parseString("#123456")::class.java.simpleName)
            //println(JsonParser.parseString("123456")::class.java.simpleName)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

}