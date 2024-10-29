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

package com.log.vastgui.core.json

import com.ave.vastgui.core.extension.SingletonHolder
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.Strictness
import com.google.gson.stream.JsonReader
import java.io.StringReader

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/29

/**
 * Gson converter
 *
 * @since 0.5.2
 */
class GsonConverter private constructor(override val isPretty: Boolean) : Converter {

    private val gson = GsonBuilder().apply {
        if (isPretty) {
            setPrettyPrinting()
        }
    }.create()

    override fun toJson(data: Any): String =
        runCatching { gson.toJson(data) }.getOrDefault(data.toString())

    override fun parseString(jsonString: String): String = runCatching {
        val reader = JsonReader(StringReader(jsonString))
            .apply { strictness = Strictness.STRICT }
        toJson(JsonParser.parseReader(reader).asJsonObject)
    }.getOrDefault(jsonString)

    companion object : SingletonHolder<GsonConverter, Boolean>(::GsonConverter)

}