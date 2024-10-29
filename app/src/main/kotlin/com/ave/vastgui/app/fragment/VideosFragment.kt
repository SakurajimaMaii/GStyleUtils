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
import com.ave.vastgui.adapter.BaseBindPagingAdapter
import com.ave.vastgui.app.BR
import com.ave.vastgui.app.adapter.entity.VideoDiffUtil
import com.ave.vastgui.app.databinding.FragmentVideosBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.app.viewmodel.SharedVM
import com.ave.vastgui.tools.bean.UserBean
import com.ave.vastgui.tools.fragment.VastVbVmFragment
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.log.vastgui.android.lifecycle.LogLifecycle
import com.log.vastgui.android.lifecycle.LogLifecycleEvent
import com.log.vastgui.core.annotation.LogExperimental
import com.log.vastgui.core.base.LogTag
import kotlinx.coroutines.launch

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/tools/app-entry-points/fragments/fragment/
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

@LogLifecycle("MyFragment", [LogLifecycleEvent.ON_CREATE, LogLifecycleEvent.ON_RESUME])
class VideosFragment : VastVbVmFragment<FragmentVideosBinding, SharedVM>() {

    private val mAdapter by lazy {
        BaseBindPagingAdapter(requireContext(), BR.video, VideoDiffUtil)
    }
    private val mLogcat = logFactory("VideosFragment")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter.setOnItemClickListener { _, position, item ->
            SimpleToast.showShortMsg("位置是$position，数据是${item?.userName}")
        }

        getBinding().imagesRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launch {
//            getViewModel().videoFlow.collectLatest {
//                mAdapter.submitData(it)
//            }
        }
    }

    override fun onPause() {
        super.onPause()
        eObjectUsage()
    }

    @OptIn(LogExperimental::class)
    fun eObjectUsage() {
        val tag = LogTag(TAG)
        mLogcat.e(tag, Exception(HELLO_WORLD))
        mLogcat.e(tag, person)
        mLogcat.e(tag) { person }
        mLogcat.e(tag, person, Exception(HELLO_WORLD))
        mLogcat.e(tag, Exception(HELLO_WORLD)) { person }
        mLogcat.e(tag, Exception(HELLO_WORLD))
        mLogcat.e(person)
        mLogcat.e { person }
        mLogcat.e(person, Exception(HELLO_WORLD))
        mLogcat.e(Exception(HELLO_WORLD)) { person }
        mLogcat.e(Exception(HELLO_WORLD))
    }

    companion object {
        private const val TAG = "VideosFragment"
        private const val HELLO_WORLD = "Hello World."
        private val person = UserBean("Ming", "19")
    }
}