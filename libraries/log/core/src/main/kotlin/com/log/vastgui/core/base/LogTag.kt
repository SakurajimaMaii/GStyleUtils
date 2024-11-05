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

package com.log.vastgui.core.base

import com.log.vastgui.core.annotation.LogExperimental

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/11

/** @since 1.3.8 */
@LogExperimental
interface Tag {
    /** @since 1.3.8 */
    val tag: String

    /** @since 1.3.8 */
    operator fun invoke() = tag
}

/** @since 1.3.8 */
@LogExperimental
data class LogTag(override val tag: String) : Tag