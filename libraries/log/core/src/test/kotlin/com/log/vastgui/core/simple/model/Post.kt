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

package com.log.vastgui.core.simple.model

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/23 18:24

data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val author: User,
    val comments: List<Comment>,
    val tags: Set<String>
)

val post = Post(
    id = 1,
    title = "Tips for Effective Kotlin Coding",
    content = "Learn these tips to write cleaner and more efficient Kotlin code...",
    author = user,
    comments = listOf(comment1, comment2),
    tags = setOf("Kotlin", "Programming", "Tutorial")
)