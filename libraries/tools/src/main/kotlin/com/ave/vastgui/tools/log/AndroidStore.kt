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

package com.ave.vastgui.tools.log

import android.os.Build
import androidx.annotation.IntRange
import com.ave.vastgui.tools.log.AndroidStore.Companion.fileNameTimeSdf
import com.ave.vastgui.tools.log.base.LogScope
import com.ave.vastgui.tools.log.base.LogScope.ExceptionStorage
import com.ave.vastgui.tools.log.base.LogSp
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.AppUtils
import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogStore
import com.log.vastgui.core.format.DEFAULT_MAX_PRINT_TIMES
import com.log.vastgui.core.format.DEFAULT_MAX_SINGLE_LOG_LENGTH
import com.log.vastgui.core.format.TableFormat
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/13 23:36
// Documentation: https://ave.entropy2020.cn/documents/tools/log/store/

/**
 * Android LogStore.
 *
 * ```kotlin
 * val mLogFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogStorage) {
 *         logStore = LogStore.android()
 *     }
 * }
 * ```
 *
 * @since 1.3.1
 */
@JvmOverloads
fun LogStore.Companion.android(
    fileRoot: File = File(FileMgr.appInternalFilesDir(), "log"),
    fileNamePrefix: String = AppUtils.getAppName(),
    fileNameDateSuffixSdf: SimpleDateFormat = fileNameTimeSdf,
    @IntRange(from = 0L, to = Long.MAX_VALUE) fileMaxSize: Long = 1000 * 1024L,
    logFormat: LogFormat = TableFormat.LogHeader.default.let {
        TableFormat(DEFAULT_MAX_SINGLE_LOG_LENGTH, DEFAULT_MAX_PRINT_TIMES, it)
    }
): AndroidStore =
    AndroidStore(fileRoot, fileNamePrefix, fileNameDateSuffixSdf, fileMaxSize, logFormat)

/**
 * Android log store.
 *
 * @property fileRoot Folder to store log files.
 * @property fileNamePrefix File name prefix.
 * @property fileNameDateSuffixSdf Date format of file name date suffix.
 * @property fileMaxSize The size of a single log file(in bytes).
 * @property logFormat The log format in file.
 * @property mFileName The name of the log file.
 * @property mLogSp LogSp is used to save the log file name of the last
 * operation.
 * @property mCurrentFile Current file which will save log.
 * @since 1.3.1
 */
class AndroidStore internal constructor(
    val fileRoot: File,
    val fileNamePrefix: String,
    val fileNameDateSuffixSdf: SimpleDateFormat,
    val fileMaxSize: Long,
    override val logFormat: LogFormat
) : LogScope(), LogStore {

    private val mFileName: String
        get() = "${fileNamePrefix}_${fileNameDateSuffixSdf.format(System.currentTimeMillis())}.log"

    private val mLogSp by lazy { LogSp() }

    private var mCurrentFile = getCurrentFile()

    override fun store(logInfo: LogInfo) {
        mLogScope.launch { mLogChannel.send(logInfo) }
    }

    /**
     * Storage the [logInfo] to file.
     *
     * @since 0.5.3
     */
    private fun storage(logInfo: LogInfo) {
        val message = logFormat.format(logInfo)
        val currentNeedSize = mCurrentFile.getCurrentSize() + message.toByteArray().size.toLong()
        if (currentNeedSize > fileMaxSize) {
            mCurrentFile = getCurrentFile(true)
        }
        mCurrentFile.storage(message)
    }

    /**
     * Get file.
     *
     * @since 0.5.3
     */
    private fun getCurrentFile(appendFile: Boolean = false): File {
        if (mLogSp.mCurrentFileName == LogSp.DEFAULT_FILE_NAME) {
            mLogSp.mCurrentFileName = mFileName
        }
        if (appendFile) {
            mLogSp.mCurrentFileName = mFileName
        }
        if (!fileRoot.exists()) {
            FileMgr.makeDir(fileRoot).result.onFailure { throw it }
        }
        val file = File(fileRoot, mLogSp.mCurrentFileName)
        if (!file.exists()) {
            FileMgr.saveFile(file).result
                .onSuccess { return file }.onFailure { throw it }
        }
        return file
    }

    /**
     * Save the [message] to the specified file.
     *
     * @since 0.5.3
     */
    private fun File.storage(message: String) {
        val fileWriter = FileWriter(this, true)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(message)
        bufferedWriter.newLine()
        bufferedWriter.close()
    }

    /**
     * Get current size of the specified file.
     *
     * @since 0.5.3
     */
    private fun File.getCurrentSize(): Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Files.readAttributes(toPath(), BasicFileAttributes::class.java).size()
    } else {
        length()
    }

    init {
        mLogScope.launch {
            while (isActive) {
                val info = mLogChannel.receive()
                storage(info)
            }
        }

        exceptionStorage = ExceptionStorage { _, exception ->
            val threadName = Thread.currentThread().name
            val info = LogInfo(threadName, exception.stackTrace[0], LogLevel.ERROR, TAG,
                System.currentTimeMillis(), exception.stackTraceToString(), exception)
            storage(info)
        }
    }

    companion object {
        const val TAG = "AndroidStore"

        /** @since 1.3.1 */
        internal val fileNameTimeSdf = SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)
    }

}