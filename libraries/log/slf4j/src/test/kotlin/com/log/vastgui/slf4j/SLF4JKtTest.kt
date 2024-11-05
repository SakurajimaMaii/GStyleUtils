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

import com.log.vastgui.core.annotation.LogExperimental
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.Delegates

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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/9
// Documentation: 
// Reference: 

@LogExperimental
class SLF4JKtTest {

    private var logger: Logger by Delegates.notNull()

    @Test
    fun mapUsage() {
        println(mapOf("1" to 1, "2" to 2))
    }

    @Test
    fun simpleTest() {
        Slf4jProvider.Options.setFactory(logFactory)
        logger = LoggerFactory.getLogger("SLF4JKtTest")
        logger.debug("This is a message")
    }

}