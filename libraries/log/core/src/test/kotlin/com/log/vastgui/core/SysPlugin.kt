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

package com.log.vastgui.core/*
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

import com.log.vastgui.core.LogPipeline.Companion.Before
import com.log.vastgui.core.base.LogPlugin

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/29
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/advanced/advanced/

class SysPlugin private constructor() {

    internal var isFirst = true

    companion object : LogPlugin<Unit, SysPlugin> {
        override val key: String = SysPlugin::class.java.simpleName

        override fun install(plugin: SysPlugin, scope: LogCat) {
            scope.logPipeline.intercept(Before) {
                if (plugin.isFirst) {
                    val head = """
                    .__                                              
                    |  |   ____   ____     ____  ___________   ____  
                    |  |  /  _ \ / ___\  _/ ___\/  _ \_  __ \_/ __ \ 
                    |  |_(  <_> ) /_/  > \  \__(  <_> )  | \/\  ___/ 
                    |____/\____/\___  /   \___  >____/|__|    \___  >
                               /_____/        \/                  \/ 
                    """.trimIndent()
                    println(head)
                    plugin.isFirst = false
                }
            }
        }

        override fun configuration(config: Unit.() -> Unit): SysPlugin {
            return SysPlugin()
        }
    }
}