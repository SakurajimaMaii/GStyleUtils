/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.app.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.adapter.BR
import com.ave.vastgui.adapter.BaseBindAdapter
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Images
import com.ave.vastgui.app.databinding.FragmentImagesBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.app.net.OpenApi
import com.ave.vastgui.app.net.OpenApiService
import com.ave.vastgui.tools.fragment.VastVbFragment
import com.ave.vastgui.tools.network.request.create
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/2 20:33
// Documentation: https://ave.entropy2020.cn/documents/tools/app-entry-points/fragments/fragment/

class ImagesFragment : VastVbFragment<FragmentImagesBinding>() {

    private val mAdapter by lazy { BaseBindAdapter<Images.Image>(requireContext(), BR.image) }
    private val mLogger = logFactory.getLogCat(VideosFragment::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getBinding().imagesRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            if (childCount > 0) {
                removeAllViews()
                mAdapter.clear()
            }
        }

        getBinding().refresh.setOnRefreshListener {
            lifecycleScope.launch {
                val images = OpenApi().create(OpenApiService::class.java)
                    .getImages(0, 10)
                    .result?.list ?: emptyList()
                mAdapter.add(images, R.layout.item_image_default)
            }
        }
    }

}