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

package com.ave.vastgui.core

import kotlin.test.Test

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/29
// Description: 
// Documentation:
// Reference: 

class StandardTest {

    private var resource: String? = "Hello"

    @Test
    fun use_and_set_to_null() {
        resource = resource.letThenNull {
            println(it.length)
        }
    }

}