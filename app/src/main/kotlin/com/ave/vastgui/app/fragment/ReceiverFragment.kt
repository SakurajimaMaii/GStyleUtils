/*
 * Copyright 2022 VastGui guihy2019@gmail.com
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ave.vastgui.app.R
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.app.databinding.FragmentReceiverBinding
import com.ave.vastgui.app.viewmodel.SharedVM
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/tools/app-entry-points/fragments/fragment/

class ReceiverFragment : Fragment(R.layout.fragment_receiver) {

    private val mViewModel by viewModels<SharedVM>({ requireActivity() })
    private val mBinding by viewBinding(FragmentReceiverBinding::bind)
    private val mLogcat = logFactory.getLogCat(ReceiverFragment::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.getSentenceAutoHandle()

        mViewModel.sentence.observe(requireActivity()) {
            mBinding.sentence.text = it.toString()
        }

        mViewModel.sentence.observeState(requireActivity()) {
            onSuccess = {
                mLogcat.d("请求成功")
            }
            onError = {
                mLogcat.d("遇到异常${it?.message}")
            }
            onFailed = { code, message ->
                mLogcat.d("请求失败，错误代码${code}，错误信息${message}")
            }
        }
    }

}