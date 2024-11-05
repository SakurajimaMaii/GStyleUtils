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

package com.ave.vastgui.netstatelayout

import android.content.Context
import android.view.ViewStub
import androidx.annotation.LayoutRes
import com.ave.vastgui.netstatelayout.listener.OnEmptyDataListener
import com.ave.vastgui.netstatelayout.listener.OnLoadingErrorListener
import com.ave.vastgui.netstatelayout.listener.OnLoadingListener
import com.ave.vastgui.netstatelayout.listener.OnNetErrorListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/11/16
// Documentation: https://ave.entropy2020.cn/documents/net-state-layout/usage/

/**
 * [NetStateMgr] used to manage the page of different states.
 *
 * Currently mainly supports fellowing states: [NetState.EMPTY_DATA],
 * [NetState.NET_ERROR], [NetState.LOADING_ERROR], [NetState.LOADING].
 *
 * If you want to customize the page, you can use [setEmptyDataView],
 * [setNetErrorView], [setLoadingView], [setLoadingView], otherwise the
 * default page will be used.
 *
 * @since 1.1.1
 */
class NetStateMgr(private val context: Context) {

    internal lateinit var loadingVs: ViewStub
        private set

    internal lateinit var netErrorVs: ViewStub
        private set

    internal lateinit var emptyDataVs: ViewStub
        private set

    internal lateinit var loadingErrorVs: ViewStub
        private set

    /** It will be called when the layout state is [NetState.LOADING]. */
    internal var mOnLoadingListener: OnLoadingListener? = null

    /** It will be called when the layout state is [NetState.NET_ERROR]. */
    internal var mOnNetErrorListener: OnNetErrorListener? = null

    /** It will be called when the layout state is [NetState.EMPTY_DATA]. */
    internal var mOnEmptyDataListener: OnEmptyDataListener? = null

    /** It will be called when the layout state is [NetState.LOADING_ERROR]. */
    internal var mOnLoadingErrorListener: OnLoadingErrorListener? = null

    /**
     * Set [loadingViewId] for [loadingVs].
     *
     * @param loadingViewId Your custom net loading view id.
     */
    fun setLoadingView(@LayoutRes loadingViewId: Int) = apply {
        loadingVs = ViewStub(context).apply {
            layoutResource = loadingViewId
        }
    }

    /**
     * Set [netErrorRetryViewId] for [netErrorVs].
     *
     * @param netErrorRetryViewId Your custom net error view id.
     */
    fun setNetErrorView(@LayoutRes netErrorRetryViewId: Int) = apply {
        netErrorVs = ViewStub(context).apply {
            layoutResource = netErrorRetryViewId
        }
    }

    /**
     * Set [emptyDataRetryViewId] for [emptyDataVs].
     *
     * @param emptyDataRetryViewId Your custom empty data view id.
     */
    fun setEmptyDataView(@LayoutRes emptyDataRetryViewId: Int) = apply {
        emptyDataVs = ViewStub(context).apply {
            layoutResource = emptyDataRetryViewId
        }
    }

    /**
     * Set [loadingErrorRetryViewId] for [loadingErrorVs].
     *
     * @param loadingErrorRetryViewId Your custom loading error view id.
     */
    fun setLoadingErrorView(@LayoutRes loadingErrorRetryViewId: Int) = apply {
        loadingErrorVs = ViewStub(context).apply {
            layoutResource = loadingErrorRetryViewId
        }
    }

    /**
     * You can set the view click event including the following status:**NET
     * ERROR**.
     *
     * @since 1.1.1
     */
    fun setOnNetErrorListener(listener: OnNetErrorListener?) = apply {
        mOnNetErrorListener = listener
    }

    /**
     * You can set the view click event including the following
     * status:**LOADING**.
     *
     * @since 1.1.1
     */
    fun setOnLoadingListener(listener: OnLoadingListener?) = apply {
        mOnLoadingListener = listener
    }

    /**
     * You can set the view click event including the following status:**EMPTY
     * DATA**.
     *
     * @since 1.1.1
     */
    fun setOnEmptyDataListener(listener: OnEmptyDataListener?) = apply {
        mOnEmptyDataListener = listener
    }

    /**
     * You can set the view click event including the following
     * status:**LOADING ERROR**.
     *
     * @since 1.1.1
     */
    fun setOnLoadingErrorListener(listener: OnLoadingErrorListener?) = apply {
        mOnLoadingErrorListener = listener
    }

    init {
        // Load default loading view.
        setLoadingView(R.layout.default_loading)
        // Load net error view.
        setNetErrorView(R.layout.default_net_error)
        // Load default empty data view.
        setEmptyDataView(R.layout.default_empty_data)
        // Load default loading error view.
        setLoadingErrorView(R.layout.default_loading_error)
    }

}