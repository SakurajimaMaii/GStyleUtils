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

package com.ave.vastgui.app.activity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityRatingBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.view.ratingview.RatingView
import com.ave.vastgui.tools.view.ratingview.StarSelectMethod
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: sakurajimamai2020@qq.com
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/rating/rating-view/

class RatingActivity : AppCompatActivity(R.layout.activity_rating) {

    private val mBinding by viewBinding(ActivityRatingBinding::bind)
    private val mLogger = logFactory(RatingActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.image.setImageBitmap(AppUtils.getAppBitmap())

        mBinding.ratingView.refreshWithInvalidate {
            setStarCountNumber(5)
            setStarBitmapSize(40F.DP, 40F.DP)
            setStarIntervalWidth(10F.DP)
            setStarSelectMethod(StarSelectMethod.UNABLE)
            setStarRating(0.8f)
            setOnStarRatingChangeListener(object : RatingView.OnStarRatingChangeListener {
                override fun onRatingChanged(rating: Float) {
                    mLogger.d("当前星星评级为 $rating")
                }
            })
        }

        mBinding.ratingView.setOnClickListener {
            SimpleToast.showShortMsg("这是一个点击事件")
        }
    }

}