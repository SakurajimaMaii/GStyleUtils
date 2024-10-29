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

package com.log.vastgui.core.simple

import com.log.vastgui.core.annotation.LogExperimental
import com.log.vastgui.core.base.LogTag
import com.log.vastgui.core.logFactory
import com.log.vastgui.core.simple.model.address1
import com.log.vastgui.core.simple.model.comment1
import com.log.vastgui.core.simple.model.post
import com.log.vastgui.core.simple.model.user
import com.log.vastgui.core.simple.model.userProfile
import org.junit.Test

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/19 23:49

private val jsonString = arrayOf(
    """
    {
      "name": "Alice",
      "age": 30,
      "isStudent": false
    }
    """.trimIndent(),
    """
    [
      "apple",
      42,
      true,
      null,
      {"key": "value"},
      [1, 2, 3]
    ]    
    """.trimIndent(),
    """
    {
      "emptyObject": {},
      "emptyArray": []
    }
    """.trimIndent(),
    """
    {
      "person": {
        "firstName": "Bob",
        "lastName": "Smith",
        "address": {
          "street": "123 Main St",
          "city": "New York"
        }
      },
      "hobbies": ["reading", "cycling", ["swimming", "skiing"]]
    }
    """.trimIndent(),
    """
    {
      "quote": "He said, \"Hello, world!\"",
      "escape": "A newline character is \\n."
    }
    """.trimIndent(),
    """
    {
      "minInt": -9007199254740991,
      "maxInt": 9007199254740991,
      "minFloat": -1.7976931348623157e+308,
      "maxFloat": 1.7976931348623157e+308
    }    
    """.trimIndent(),
    """
    {
      "trueValue": true,
      "falseValue": false,
      "nullValue": null
    }    
    """.trimIndent()
)

private val objs = arrayOf(
    user, address1, userProfile, comment1, post
)

class SimpleKtTest {

    private val logcat = logFactory("tag")

    private data class Person(val name: String, val age: Int)

    @OptIn(LogExperimental::class)
    @Test
    fun usage() {
        val tag = LogTag(TAG)
        logcat.i(tag, HELLO_WORLD)
    }

    @OptIn(LogExperimental::class)
    @Test
    fun iSimpleUsage() {
        val tag = LogTag(TAG)
        logcat.i(tag, HELLO_WORLD)
        logcat.i(tag) { HELLO_WORLD }
        logcat.i(tag, HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.i(tag, Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.i(HELLO_WORLD)
        logcat.i { HELLO_WORLD }
        logcat.i(HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.i(Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.i(Exception(HELLO_WORLD))
    }

    @OptIn(LogExperimental::class)
    @Test
    fun vSimpleUsage() {
        val tag = LogTag(TAG)
        logcat.v(tag, HELLO_WORLD)
        logcat.v(tag) { HELLO_WORLD }
        logcat.v(tag, HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.v(tag, Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.v(HELLO_WORLD)
        logcat.v { HELLO_WORLD }
        logcat.v(HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.v(Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.v(Exception(HELLO_WORLD))
    }

    @OptIn(LogExperimental::class)
    @Test
    fun wSimpleUsage() {
        val tag = LogTag(TAG)
        logcat.w(tag, HELLO_WORLD)
        logcat.w(tag) { HELLO_WORLD }
        logcat.w(tag, HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.w(tag, Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.w(HELLO_WORLD)
        logcat.w { HELLO_WORLD }
        logcat.w(HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.w(Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.w(Exception(HELLO_WORLD))
    }

    @OptIn(LogExperimental::class)
    @Test
    fun eSimpleUsage() {
        val tag = LogTag(TAG)
        logcat.e(tag, HELLO_WORLD)
        logcat.e(tag) { HELLO_WORLD }
        logcat.e(tag, HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.e(tag, Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.e(tag, Exception(HELLO_WORLD))
        logcat.e(HELLO_WORLD)
        logcat.e { HELLO_WORLD }
        logcat.e(HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.e(Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.e(Exception(HELLO_WORLD))
    }

    @OptIn(LogExperimental::class)
    @Test
    fun dSimpleUsage() {
        val tag = LogTag(TAG)
        logcat.d(tag, HELLO_WORLD)
        logcat.d(tag) { HELLO_WORLD }
        logcat.d(tag, HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.d(tag, Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.d(tag, Exception(HELLO_WORLD))
        logcat.d(HELLO_WORLD)
        logcat.d { HELLO_WORLD }
        logcat.d(HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.d(Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.d(Exception(HELLO_WORLD))
    }

    @OptIn(LogExperimental::class)
    @Test
    fun aSimpleUsage() {
        val tag = LogTag(TAG)
        logcat.a(tag, HELLO_WORLD)
        logcat.a(tag) { HELLO_WORLD }
        logcat.a(tag, HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.a(tag, Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.a(tag, Exception(HELLO_WORLD))
        logcat.a(HELLO_WORLD)
        logcat.a { HELLO_WORLD }
        logcat.a(HELLO_WORLD, Exception(HELLO_WORLD))
        logcat.a(Exception(HELLO_WORLD)) { HELLO_WORLD }
        logcat.a(Exception(HELLO_WORLD))
    }

    @OptIn(LogExperimental::class)
    @Test
    fun eObjectUsage() {
        val tag = LogTag(TAG)
        logcat.e(tag, Exception(HELLO_WORLD))
        logcat.e(tag, person)
        logcat.e(tag) { person }
        logcat.e(tag, person, Exception(HELLO_WORLD))
        logcat.e(tag, Exception(HELLO_WORLD)) { person }
        logcat.e(tag, Exception(HELLO_WORLD))
        logcat.e(person)
        logcat.e { person }
        logcat.e(person, Exception(HELLO_WORLD))
        logcat.e(Exception(HELLO_WORLD)) { person }
        logcat.e(Exception(HELLO_WORLD))

        objs.forEach { logcat.e(it) }
    }

    @Test
    fun jsonPrettyUsage() {
        val json = """
            {
                    "minInt": -9007199254740991,
                                "maxInt": 9007199254740991,
                    "minFloat": -1.7976931348623157e+308,
                "maxFloat": 1.7976931348623157e+308
                }   
        """.trimIndent()
        logcat.d(json)
    }

    @Test
    fun paragraph() {
        val paragraph = """
            苏州园林里都有假山和池沼。假山的堆叠，可以说是一项艺术而不仅是技术。或者是重峦叠嶂，或者是几座小山配合着竹子花木，
            全在乎设计者和匠师们生平多阅历，胸中有邱壑，才能使游览者攀登的时候忘却苏州城市，只觉得身在山间。至于池沼，大多引
            用活水。有些园林池沼宽敞，就把池沼作为全园的中心，其他景物配合着布置。水面假如成河道模样，往往安排桥梁。假如安排
            两座以上的桥梁，那就一座一个样，决不雷同。池沼或河道的边沿很少砌齐整的石岸，总是高低屈曲任其自然。还在那儿布置几
            块玲珑的石头，或者种些花草：这也是为了取得从各个角度看都成一幅画的效果。池沼里养着金鱼或各色鲤鱼，夏秋季节荷花或
            睡莲开放，游览者看“鱼戏莲叶间”，又是入画的一景。
            """.trimIndent()
        logcat.d(paragraph)
    }

    @OptIn(LogExperimental::class)
    @Test
    fun nullUsage() {
        val tag = LogTag(TAG)
        logcat.e(tag, null)
        logcat.e(tag, null, Exception(HELLO_WORLD))
        logcat.e(tag, Exception())
        logcat.e(null)
        logcat.e(null, Exception(HELLO_WORLD))
        logcat.e(Exception())
    }

    companion object {
        private const val TAG = "SimpleTest"
        private const val HELLO_WORLD = "Hello World."
        private val person = Person("Ming", 19)
    }

}