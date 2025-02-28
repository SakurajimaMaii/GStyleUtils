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

package com.ave.vastgui.adapter.listener

import android.view.View
import com.ave.vastgui.adapter.base.ItemWrapper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/12/27
// Documentation: https://ave.entropy2020.cn/documents/adapter/

/**
 * Interface definition for a callback to be invoked when a view is clicked in list item.
 *
 * @since 1.1.1
 */
fun interface OnItemChildClickListener<T : Any> {
    /**
     * Called when the [view] has been clicked.
     *
     * @param item The currently clicked list item data. If it is empty, it means
     * the current list is empty.
     * @since 1.2.0
     */
    fun onItemClick(view: View, pos: Int, item: T?)
}