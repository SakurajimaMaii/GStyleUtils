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

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import com.ave.vastgui.app.activity.log.mLogFactory
import com.ave.vastgui.app.databinding.ActivityArcProgressViewBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.download.DLManager
import com.ave.vastgui.tools.utils.download.DLTask
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.view.extension.viewSnapshot
import com.ave.vastgui.tools.view.toast.SimpleToast
import java.io.File

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:42
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/arc-progress-view/

class ArcProgressViewActivity : VastVbActivity<ActivityArcProgressViewBinding>() {

    private val logger = mLogFactory.getLog(ArcProgressViewActivity::class.java)
    private lateinit var downloadTask: DLTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colors = intArrayOf(
            ColorUtils.colorHex2Int("#F60C0C"),
            ColorUtils.colorHex2Int("#F3B913"),
            ColorUtils.colorHex2Int("#E7F716"),
            ColorUtils.colorHex2Int("#3DF30B"),
            ColorUtils.colorHex2Int("#0DF6EF"),
            ColorUtils.colorHex2Int("#0829FB"),
            ColorUtils.colorHex2Int("#B709F4")
        )
        val pos = floatArrayOf(
            1f / 7, 2f / 7, 3f / 7, 4f / 7, 5f / 7, 6f / 7, 1f
        )

        getBinding().arcProgressView.apply {
            setProgressShader(
                LinearGradient(
                    -700f, 0f, 700f, 0f,
                    colors, pos,
                    Shader.TileMode.CLAMP
                )
            )
            mProgressWidth = 10f.DP
        }

        getBinding().arcProgressView.setOnClickListener {
            val bitmap = viewSnapshot(it)
            BmpUtils.saveBitmapAsFile(
                bitmap = bitmap,
                File(FileMgr.appInternalFilesDir(), "width_10.jpg")
            )?.apply {
                SimpleToast.showShortMsg("截图${name}已保存")
            }
        }

        getBinding().download.setOnClickListener {
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
        downloadTask = DLManager
            .createTaskConfig()
            .setDownloadUrl("https://down.oray.com/sunlogin/windows/SunloginClient_13.3.1.56398_x64.exe")
            .setSaveDir(FileMgr.appInternalFilesDir().path)
            .setListener {
                onDownloading = {
                    getBinding().arcProgressView.refreshWithInvalidate {
                        setCurrentProgress(
                            it.rate * getBinding().arcProgressView.mMaximumProgress
                        )
                    }
                }
                onFailure = {
                    logger.e("download failed:" + it.exception.stackTraceToString())
                }
                onSuccess = {
                    logger.i("download success.")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        setCurrentProgress(getBinding().arcProgressView.mMaximumProgress)
                    }
                }
                onCancel = {
                    logger.i("download cancel.")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        resetProgress()
                    }
                }
            }
            .build()

        downloadTask.start()
    }

}