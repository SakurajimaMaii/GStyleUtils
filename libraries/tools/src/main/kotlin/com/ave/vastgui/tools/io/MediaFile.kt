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

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.Media
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.ave.vastgui.tools.content.ContextHelper
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/2

/**
 * Media file.
 *
 * @since 1.5.2
 */
sealed interface MediaFile {

    /** @since 1.5.2 */
    val file: File

    /** @since 1.5.2 */
    fun getDefaultFileName(): String

    /**
     * Get the uri by [file].
     *
     * [uri] will insert a new row into a table at the given URL
     * [Media.EXTERNAL_CONTENT_URI]. So when you want to delete the file,
     * please also run [android.content.ContentResolver.delete] to make sure
     * that the file-related information is completely deleted. For example:
     * ```kotlin
     * contentResolver.delete(
     *      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
     *      MediaStore.Audio.Media.DISPLAY_NAME + "=?",
     *      arrayOf("xxx.jpg")
     * )
     * ```
     *
     * By default, [uri] will only insert the following columns: [Media.DATA],
     * [Media.DISPLAY_NAME], [Media.MIME_TYPE], If you want to customize, you
     * can use [contentValues]
     *
     * @since 1.5.2
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun uri(contentValues: ContentValues.() -> Unit = {}): Uri? {
        val values = ContentValues().also(contentValues).apply {
            put(Media.DATA, file.absolutePath)
            put(Media.DISPLAY_NAME, file.name)
            put(Media.MIME_TYPE, file.mimeType())
        }
        return ContextHelper.getAppContext().contentResolver.insert(Media.EXTERNAL_CONTENT_URI, values)
    }

    /**
     * Get the uri by [file].
     *
     * Please register a provider in AndroidManifest.xml. For example:
     * ```xml
     * <provider
     *      android:name="androidx.core.content.FileProvider"
     *      android:authorities="${applicationId}"
     *      android:exported="false"
     *      android:grantUriPermissions="true">
     *      <meta-data
     *          android:name="android.support.FILE_PROVIDER_PATHS"
     *          android:resource="@xml/file_paths" />
     * </provider>
     * ```
     *
     * @param authority The authority of a [FileProvider] defined in a
     * <provider> element in your app's manifest.
     * @since 1.5.2
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun uri(authority: String): Uri = file.uri(authority)

    /**
     * Get the uri by [file].
     *
     * @since 1.5.2
     */
    fun uri(): Uri = file.uri()

}