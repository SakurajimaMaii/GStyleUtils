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

package com.ave.vastgui.app.activity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityVp2IndicatorBinding
import com.ave.vastgui.app.fragment.ImagesFragment
import com.ave.vastgui.app.fragment.ReceiverFragment
import com.ave.vastgui.app.fragment.SenderFragment
import com.ave.vastgui.app.fragment.VideosFragment
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.activity.widget.screenConfig
import com.ave.vastgui.tools.adapter.VastFragmentAdapter
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.log.vastgui.android.lifecycle.LogLifecycle

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/13 19:45
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/viewpager2/fragment-adapter/
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/viewpager2/vp2-indicator-view/vp2-indicator-view/

@LogLifecycle
class Vp2IndicatorActivity : AppCompatActivity() {

    private val mBinding by viewBinding(ActivityVp2IndicatorBinding::inflate)
    private val fragments = ArrayList<Fragment>().apply {
        add(VideosFragment())
        add(ImagesFragment())
        add(ReceiverFragment())
        add(SenderFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        screenConfig(mEnableActionBar = true, mEnableFullScreen = false)
        setSupportActionBar(mBinding.toolbar)
        mBinding.vp2.apply {
            adapter = VastFragmentAdapter(this@Vp2IndicatorActivity, fragments)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    mBinding.toolbar.title = fragments[position].defaultLogTag()
                }
            })
        }
        mBinding.vp2indicator.apply {
//            setIndicatorStyle(Vp2IndicatorType.BITMAP)
            setBitmapSize(20f.DP.toInt(), 20f.DP.toInt())
//            setSelectedBitmap(R.drawable.ic_indicator_select)
//            setUnSelectedBitmap(R.drawable.ic_indicator_unselect)
            setIndicatorCircleRadius(8F.DP)
            setSelectedColor(R.color.tomato)
            setUnSelectedColor(R.color.limegreen)
            attachToViewPager2(mBinding.vp2)
        }
    }

}