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

package com.log.vastgui.desktop

import com.ave.vastgui.core.extension.NotNullOrDefault
import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogStore
import com.log.vastgui.core.format.LineFormat
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/15 22:52
// Documentation: https://ave.entropy2020.cn/documents/log/log-desktop/store/
// Reference: https://github.com/setruth/SetruthTools/blob/master/KLogr/src/main/kotlin/org/setruth/tools/klogr/KLogr.kt

private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

/**
 * Desktop log store.
 *
 * ```kotlin
 * val logFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogStorage) {
 *         logStore = LogStore.desktop("", 1024L * 1000)
 *     }
 * }
 * ```
 *
 * @param fileRoot Folder to store log files.
 * @param fileMaxSize The size of a single log file(in bytes).
 * @param logFormat The format of log in files.
 * @see <a
 * href="https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/log/desktop/log">Example
 * log file</a>
 * @since 1.3.1
 */
fun LogStore.Companion.desktop(
    fileRoot: String,
    fileMaxSize: Long,
    logFormat: LogFormat = LineFormat
) = DesktopStore(logFormat).apply {
    this.fileRoot = fileRoot
    this.fileMaxSize = fileMaxSize
}

/**
 * Desktop store implementation.
 *
 * @since 1.3.1
 */
class DesktopStore internal constructor(override val logFormat: LogFormat) : LogStore {

    /**
     * Folder to store log files.
     *
     * @since 1.3.1
     */
    var fileRoot by NotNullOrDefault("")

    /**
     * The size of a single log file(in bytes).
     *
     * @since 1.3.1
     */
    var fileMaxSize by NotNullOrDefault(1024L)

    /** @since 1.3.1 */
    override fun store(logInfo: LogInfo) {
        storage(logInfo)
    }

    /**
     * Determine whether the folder storage path exists. If it is "",
     * create a folder named log in the current folder for log storage. If
     * it is not empty but the folder does not exist, it will be created
     * automatically. If it is not empty and exists, no processing will be done
     *
     * @since 1.3.1
     */
    private fun checkFileRoot(): String =
        if (fileRoot.isBlank()) {
            File(System.getProperty("user.dir"), "log")
        } else {
            File(fileRoot)
        }.apply {
            if (!exists()) mkdir()
        }.absolutePath

    /** @since 1.3.1 */
    private fun storage(info: LogInfo) {
        val fileRoot = checkFileRoot()
        val fileNamePrefix = sdf.format(System.currentTimeMillis())
        val fileNameList = getFileList(fileNamePrefix)
        val logFile = if (fileNameList.isEmpty()) {
            File(fileRoot, "${fileNamePrefix}(0).txt").apply {
                if (!exists()) createNewFile()
            }
        } else {
            val lastFile = fileNameList.last()
            if (lastFile.length() >= fileMaxSize) {
                File(
                    fileRoot,
                    "$fileNamePrefix(${fileNameList.size}).txt"
                ).apply { createNewFile() }
            } else {
                lastFile
            }
        }
        logFile.appendText("${logFormat.format(info)} \n")
    }

    /**
     * Get log files in the folder and order them based on the file date.
     *
     * @since 1.3.1
     */
    private fun getFileList(fileNamePrefix: String) =
        File(fileRoot).listFiles()
            ?.filter { it.name.startsWith(fileNamePrefix) }
            ?.sortedWith(compareBy({ getFileOrderNumber(it.name) }, { it.name }))
            ?: emptyList()

    /** @since 1.3.1 */
    private fun getFileOrderNumber(fileName: String): Int {
        val startIndex = fileName.indexOf('(') + 1
        val endIndex = fileName.indexOf(')')
        return fileName.substring(startIndex, endIndex).toInt()
    }

}