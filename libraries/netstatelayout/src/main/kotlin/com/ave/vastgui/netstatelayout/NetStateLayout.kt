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
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.FrameLayout
import androidx.core.util.forEach

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/11/5
// Documentation: https://ave.entropy2020.cn/documents/net-state-layout/usage/

/**
 * [NetStateLayout] is a layout to set network state page.
 *
 * You can use [showLoading] 、[showLoadingError] 、[showNetError]
 * 、[showEmptyData] to show the net state page.
 *
 * If you don't set the [mNetStateMgr] , it will take the default value
 * when you call a method like [showLoading] .
 *
 * @since 1.1.1
 */
class NetStateLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    /** Set default vastNetStateMgr. */
    lateinit var mNetStateMgr: NetStateMgr
        private set

    /** Layout sparse array. */
    private val layoutSparseArray: SparseArray<View> = SparseArray<View>()

    /** @since 1.1.1 */
    fun setNetStateMgr(mgr: NetStateMgr) {
        mNetStateMgr = mgr
        addNetStateView()
    }

    /** Add all different state layouts to the frame layout */
    private fun addNetStateView() {
        val layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mNetStateMgr.apply {
            addView(loadingVs, layoutParams)
            addView(netErrorVs, layoutParams)
            addView(emptyDataVs, layoutParams)
            addView(loadingErrorVs, layoutParams)
        }
    }

    fun showLoading() {
        if (!::mNetStateMgr.isInitialized) {
            mNetStateMgr = NetStateMgr(context)
            addNetStateView()
        }
        if (!checkVs(NetState.LOADING)) {
            val view = mNetStateMgr.loadingVs.run {
                setOnInflateListener { _, inflated ->
                    mNetStateMgr.mOnLoadingListener?.onLoadingInflate(inflated)
                }
                inflate()
            }
            view.setOnClickListener {
                mNetStateMgr.mOnLoadingListener?.onLoadingClick()
            }
            layoutSparseArray.put(NetState.LOADING.ordinal, view)
        }
        showHideViewById(NetState.LOADING)
    }

    /**
     * @param throwable Exception encountered during the request process,
     * @since 1.1.1
     */
    @JvmOverloads
    fun showNetError(throwable: Throwable? = null) {
        if (!::mNetStateMgr.isInitialized) {
            mNetStateMgr = NetStateMgr(context)
            addNetStateView()
        }
        if (!checkVs(NetState.NET_ERROR)) {
            val view = mNetStateMgr.netErrorVs.run {
                setOnInflateListener { _, inflated ->
                    mNetStateMgr.mOnNetErrorListener?.onNetErrorInflate(
                        inflated,
                        throwable
                    )
                }
                inflate()
            }
            view.setOnClickListener {
                mNetStateMgr.mOnNetErrorListener?.onNetErrorClick()
            }
            layoutSparseArray.put(NetState.NET_ERROR.ordinal, view)
        }
        showHideViewById(NetState.NET_ERROR)
    }

    /**
     * @param code Response code.
     * @param message Response message.
     * @since 1.1.1
     */
    @JvmOverloads
    fun showLoadingError(code: Int? = null, message: String? = null) {
        if (!::mNetStateMgr.isInitialized) {
            mNetStateMgr = NetStateMgr(context)
            addNetStateView()
        }
        if (!checkVs(NetState.LOADING_ERROR)) {
            val view = mNetStateMgr.loadingErrorVs.run {
                setOnInflateListener { _, inflated ->
                    mNetStateMgr
                        .mOnLoadingErrorListener
                        ?.onLoadingErrorInflate(inflated, code, message)
                }
                inflate()
            }
            view.setOnClickListener {
                mNetStateMgr.mOnLoadingErrorListener?.onLoadingErrorClick()
            }
            layoutSparseArray.put(NetState.LOADING_ERROR.ordinal, view)
        }
        showHideViewById(NetState.LOADING_ERROR)
    }

    fun showEmptyData() {
        if (!::mNetStateMgr.isInitialized) {
            mNetStateMgr = NetStateMgr(context)
            addNetStateView()
        }
        if (!checkVs(NetState.EMPTY_DATA)) {
            val view = mNetStateMgr.emptyDataVs.run {
                setOnInflateListener { _, inflated ->
                    mNetStateMgr.mOnEmptyDataListener?.onEmptyDataInflate(
                        inflated
                    )
                }
                inflate()
            }
            view.setOnClickListener {
                mNetStateMgr.mOnEmptyDataListener?.onEmptyDataClick()
            }
            layoutSparseArray.put(NetState.EMPTY_DATA.ordinal, view)
        }
        showHideViewById(NetState.EMPTY_DATA)
    }

    fun showSuccess() {
        if (!::mNetStateMgr.isInitialized) {
            mNetStateMgr = NetStateMgr(context)
            addNetStateView()
        }
        showHideViewById(NetState.SHOW_SUCCESS)
    }

    /** Show or hide view according to the [netState] . */
    private fun showHideViewById(netState: NetState) {
        layoutSparseArray.forEach { key, view ->
            if (key == netState.ordinal) {
                view.visibility = VISIBLE
            } else {
                if (view.visibility != GONE) {
                    view.visibility = GONE
                }
            }
        }
    }

    /** @since 1.1.1 */
    private fun checkVs(netState: NetState): Boolean {
        layoutSparseArray.forEach { key, _ ->
            if (key == netState.ordinal) return true
        }
        return false
    }
}