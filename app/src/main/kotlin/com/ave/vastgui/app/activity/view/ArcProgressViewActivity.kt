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

import android.Manifest
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import com.ave.vastgui.app.databinding.ActivityArcProgressViewBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.download.DLManager
import com.ave.vastgui.tools.utils.download.DLTask
import com.ave.vastgui.tools.utils.permission.requestMultiplePermissions
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import java.io.File

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:42
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/progress/arc-progress-view/

class ArcProgressViewActivity : VastVbActivity<ActivityArcProgressViewBinding>() {

    private val mLogger = logFactory.getLogCat(ArcProgressViewActivity::class.java)
    private lateinit var downloadTask: DLTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestMultiplePermissions(arrayOf(Manifest.permission.ACCESS_NETWORK_STATE))

        val colors = intArrayOf(
            ColorUtils.colorHex2Int("#F60C0C"),
            ColorUtils.colorHex2Int("#F3B913"),
            ColorUtils.colorHex2Int("#E7F716"),
            ColorUtils.colorHex2Int("#3DF30B"),
            ColorUtils.colorHex2Int("#0DF6EF"),
            ColorUtils.colorHex2Int("#0829FB"),
            ColorUtils.colorHex2Int("#B709F4")
        )
        val pos = floatArrayOf(1f / 7, 2f / 7, 3f / 7, 4f / 7, 5f / 7, 6f / 7, 1f)

        getBinding().arcProgressView.apply {
            mProgressShader = LinearGradient(
                -700f, 0f, 700f, 0f,
                colors, pos,
                Shader.TileMode.CLAMP
            )
            mEndpointCircleRadius = 15f.DP.coerceAtLeast(recommendedRadius())
            mProgressWidth = 15f.DP
            mEndpointCircleColor = ColorUtils.colorHex2Int("#eb4d4b")
        }

        getBinding().download.setOnClickListener {
            mLogger.i("开始下载")
            downloadApk()
        }

        getBinding().pause.setOnClickListener {
            downloadTask.pause()
        }

        getBinding().resume.setOnClickListener {
            downloadTask.resume()
        }

        getBinding().cancel.setOnClickListener {
            downloadTask.cancel()
        }
    }

    private fun downloadApk() {
        val root = FileMgr.appInternalFilesDir()
        val name = "Tabby.exe"
        val file = File(root, name)
        downloadTask = DLManager
            .createTaskConfig()
            .setDownloadUrl("https://objects.githubusercontent.com/github-production-release-asset-2e65be/77213120/e661ad3b-76f6-46cd-b2f3-abe38de4fc4a?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=releaseassetproduction%2F20240717%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240717T073421Z&X-Amz-Expires=300&X-Amz-Signature=bda89807f32bebb4b862b4d12ecc0834e6fca742b814288fbcac3e37f26f16ab&X-Amz-SignedHeaders=host&actor_id=46998172&key_id=0&repo_id=77213120&response-content-disposition=attachment%3B%20filename%3Dtabby-1.0.210-setup-x64.exe&response-content-type=application%2Foctet-stream")
            .setSaveDir(root.path)
            .setSaveName("Tabby.exe")
            .setListener {
                onDownloading = {
                    getBinding().arcProgressView.refreshWithInvalidate {
                        mCurrentProgress =
                            it.rate * getBinding().arcProgressView.mMaximumProgress
                    }
                }
                onFailure = {
                    mLogger.e("download failed:" + it.exception.stackTraceToString())
                }
                onSuccess = {
                    mLogger.i("download success.")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        mCurrentProgress = getBinding().arcProgressView.mMaximumProgress
                    }
                }
                onCancel = {
                    mLogger.i("download cancel.")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        resetProgress()
                    }
                }
            }
            .build()
        if (file.exists()) {
            if (FileMgr.deleteFile(file).isSuccess) {
                downloadTask.start()
            }
        } else {
            downloadTask.start()
        }
    }

}