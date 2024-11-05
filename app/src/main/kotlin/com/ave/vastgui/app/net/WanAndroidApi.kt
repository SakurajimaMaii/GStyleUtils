/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.app.net

import com.ave.vastgui.app.net.interceptor.okhttp3Interceptor
import com.ave.vastgui.tools.network.request.RequestBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/4
// Description: WanAndroid 开放 Api
// Reference: https://www.wanandroid.com/blog/show/2

class WanAndroidApi : RequestBuilder("https://www.wanandroid.com") {
    override fun retrofitConfiguration(builder: Retrofit.Builder) {
        super.retrofitConfiguration(builder)
        builder.addConverterFactory(GsonConverterFactory.create())
    }

    override fun okHttpConfiguration(builder: OkHttpClient.Builder) {
        super.okHttpConfiguration(builder)
        builder.addInterceptor(okhttp3Interceptor)
    }
}