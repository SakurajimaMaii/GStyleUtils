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

package com.ave.vastgui.tools.io

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore.Images.Media
import com.ave.vastgui.tools.content.ContextHelper
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.utils.DateUtils
import java.io.File

/** Using to provide information about media file. */
sealed interface MediaFileProperty {

    /**
     * Get application-specific storage folder.
     *
     * @param subDir The name of the subfolder you wish to create.
     * @since 0.5.0
     */
    fun getExternalFilesDir(subDir: String? = null): File?

    /**
     * Get shared storage folder.
     *
     * @since 0.5.0
     */
    fun getSharedFilesDir(): File

    /**
     * Get default file name.
     *
     * @param extension The media file extension.
     * @return For example, 20230313_234940_455_com_ave_vastgui_app.jpg.
     */
    fun getDefaultFileName(extension: String): String {
        val timeStamp: String = DateUtils.getCurrentTime("yyyyMMdd_HHmmss_SSS")
        return try {
            "${timeStamp}_${AppUtils.getPackageName().replace(".", "_")}$extension"
        } catch (_: Exception) {
            "${timeStamp}_media_file_mgr$extension"
        }
    }

    /**
     * Get file by [uri]. [getFileByUri] will query the [Media.DATA] field
     * corresponding to [uri], and return the file if there is a corresponding
     * path, null otherwise.
     *
     * @return file, null otherwise.
     */
    fun getFileByUri(uri: Uri): File? {
        val proj = arrayOf(Media.DATA)
        val cursor: Cursor? = ContextHelper.getAppContext().contentResolver.query(uri, proj, null, null, null)
        if (null == cursor) return null

        if (cursor.moveToFirst()) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(Media.DATA)
            val path: String = cursor.getString(columnIndex)
            cursor.close()
            return File(path)
        }

        return null
    }

}