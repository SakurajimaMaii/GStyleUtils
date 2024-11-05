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

package com.ave.vastgui.app.activity.view.rvadapter

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.adapter.BR
import com.ave.vastgui.adapter.BaseAdapter
import com.ave.vastgui.adapter.BaseBindAdapter
import com.ave.vastgui.adapter.BaseListAdapter
import com.ave.vastgui.adapter.BasePagingAdapter
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.ImageDiffUtil
import com.ave.vastgui.app.adapter.entity.Images
import com.ave.vastgui.app.adapter.holder.ComicImageHolder
import com.ave.vastgui.app.adapter.holder.DefaultImageHolder
import com.ave.vastgui.app.databinding.ActivityImageBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.app.net.OpenApi
import com.ave.vastgui.app.net.OpenApiService
import com.ave.vastgui.app.viewmodel.NetVM
import com.ave.vastgui.tools.activity.VastVbVmActivity
import com.ave.vastgui.tools.network.request.create
import com.ave.vastgui.tools.view.dialog.MaterialAlertDialogBuilder
import com.ave.vastgui.tools.view.toast.SimpleToast.showShortMsg
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/4
// Documentation: https://ave.entropy2020.cn/documents/adapter/

private class ImageAdapter(context: Context) : BaseAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion)
)

private class ImageBindAdapter(context: Context) :
    BaseBindAdapter<Images.Image>(context, BR.image)

private class ImageListAdapter(context: Context) : BaseListAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion), ImageDiffUtil
)

private class ImagePagingAdapter(context: Context) : BasePagingAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion), ImageDiffUtil
)

class ImageActivity : VastVbVmActivity<ActivityImageBinding, NetVM>() {

    private val logcat = logFactory.getLogCat(this::class.java)

    private val mImageAdapter: ImageAdapter by lazy {
        ImageAdapter(this)
    }
    private val mImageListAdapter: ImageListAdapter by lazy {
        ImageListAdapter(this)
    }
    private val mImagePagingAdapter: ImagePagingAdapter by lazy {
        ImagePagingAdapter(this)
    }
    private val mImageBindAdapter: ImageBindAdapter by lazy {
        ImageBindAdapter(this)
    }

    private val showDialog = OnItemClickListener<Images.Image> { view, _, _ ->
        MaterialAlertDialogBuilder(view.context).setMessage("这是一个点击事件").show()
    }
    private val showSnackBar = OnItemClickListener<Images.Image> { _, _, _ ->
        getSnackbar().setText("列表项被点击").show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImageBindAdapter.apply {
            setOnItemClickListener { _, pos, _ ->
                showShortMsg("Click event and pos is $pos.")
            }
            setOnItemLongClickListener { _, pos, _ ->
                showShortMsg("Long click event and pos is $pos.")
                true
            }
        }

        testBasePagingAdapter()
    }

    private fun testBaseBindAdapter() {
        getBinding().images.layoutManager = LinearLayoutManager(this)
        getBinding().images.adapter = mImageBindAdapter.apply {
            setEmptyView(R.layout.page_empty_default)
        }
        getBinding().clear.setOnClickListener {
            mImageBindAdapter.clear()
        }
        getBinding().removeFirst.setOnClickListener {
            mImageBindAdapter.removeAt(0)
        }
        getBinding().load.setOnClickListener {
            lifecycleScope.launch {
                val images = OpenApi()
                    .create(OpenApiService::class.java).getImages(0, 10)
                    .result?.list
                if (null == images) return@launch
                mImageBindAdapter.add(images, R.layout.item_image_default)
            }
        }
        getBinding().insert.setOnClickListener {
            val image = Images.Image(
                8008,
                "王者荣耀司马懿 暗渊魔法",
                "game",
                "https://pic.netbian.com/uploads/allimg/211213/212526-16394019264e91.jpg"
            )
            mImageBindAdapter.add(image, R.layout.item_image_default, 3)
        }
        getBinding().addEmpty1.setOnClickListener {
            mImageBindAdapter.setEmptyView(R.layout.page_empty_default)
        }
        getBinding().addEmpty2.setOnClickListener {
            mImageBindAdapter.setEmptyView(R.layout.page_empty_box)
        }
        getBinding().removeEmpty.setOnClickListener {
            mImageBindAdapter.setEmptyView(null)
        }
    }

    private fun testBaseListAdapter() {
        getBinding().images.layoutManager = LinearLayoutManager(this)
        getBinding().images.adapter = mImageListAdapter.apply {
            setLoadingView(R.layout.page_loading)
        }
        getBinding().load.setOnClickListener {
            lifecycleScope.launch {
                mImageListAdapter.submitListWithLoading()
                val list = OpenApi().create(OpenApiService::class.java)
                    .getImages(0, 20)
                    .result?.list ?: return@launch
                mImageListAdapter.submitList(list, R.layout.item_image_default)
            }
        }
        getBinding().clear.setOnClickListener {
            mImageListAdapter.submitList(emptyList<Images.Image>())
        }
        getBinding().addEmpty1.setOnClickListener {
            mImageListAdapter.setEmptyView(R.layout.page_empty_default) {
                setOnItemClickListener { _, _, _ ->
                    showShortMsg("这是第一个空白界面")
                }
                addOnItemChildClickListener(R.id.empty_default_image_root) { _, _, _ ->

                }
            }
        }
        getBinding().addEmpty2.setOnClickListener {
            mImageListAdapter.setEmptyView(R.layout.page_empty_box) {
                setOnItemClickListener { _, _, _ ->
                    showShortMsg("这是第二个空白界面")
                }
            }
        }
    }

    private fun testBasePagingAdapter() {
        getBinding().images.layoutManager = LinearLayoutManager(this)
        getBinding().images.adapter = mImagePagingAdapter
        lifecycleScope.launch {
            getViewModel().imageFlow.collect {
                mImagePagingAdapter.submitData(it)
            }
        }
    }
}