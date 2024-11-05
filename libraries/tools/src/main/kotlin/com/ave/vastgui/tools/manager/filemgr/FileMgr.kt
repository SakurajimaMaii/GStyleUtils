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

package com.ave.vastgui.tools.manager.filemgr

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.security.crypto.EncryptedFile
import com.ave.vastgui.core.ResultCompat
import com.ave.vastgui.tools.config.ToolsConfig
import com.ave.vastgui.tools.content.ContextHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/5/30
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/app-data-and-files/file-manager/file-mgr/

/**
 * File path scope
 *
 * @since 0.5.1
 */
class FilePathScope internal constructor() {
    infix fun String.f(path: String) = "$this${File.separator}$path"
}

object FileMgr {

    private val mimeTypeMap = MimeTypeMap.getSingleton()

    /**
     * @return The File which from internal storage, meant for your app's use
     * only.
     * @see [Context.getFilesDir]
     */
    @JvmStatic
    fun appInternalFilesDir(): File = ContextHelper.getAppContext().filesDir

    /**
     * @return The File which from internal storage, meant for your app's use
     * only.
     * @see [Context.getCacheDir]
     */
    @JvmStatic
    fun appInternalCacheDir(): File = ContextHelper.getAppContext().cacheDir

    /**
     * @return The File which from external storage, meant for your app's use
     * only. Throw IllegalStateException if shared storage is not currently
     * available.
     * @throws IllegalStateException
     * @see [Context.getExternalFilesDir]
     */
    @JvmStatic
    @Throws(IllegalStateException::class)
    fun appExternalFilesDir(path: String?): File = if (isExternalStorageReadable()) {
        ContextHelper.getAppContext().getExternalFilesDir(path)!!
    } else throw IllegalStateException("Shared storage is not currently available.")


    /**
     * @return The File which from external storage, meant for your app's use
     * only. Throw IllegalStateException if shared storage is not currently
     * available.
     * @see [Context.getExternalCacheDir]
     */
    @JvmStatic
    @Throws(IllegalStateException::class)
    fun appExternalCacheDir(): File = if (isExternalStorageReadable()) {
        ContextHelper.getAppContext().externalCacheDir!!
    } else throw IllegalStateException("Shared storage is not currently available.")

    /**
     * Get file path
     *
     * @param scope The scope that is used to build file path.
     * @since 0.5.1
     */
    @JvmStatic
    fun getPath(scope: FilePathScope.() -> String): String {
        return FilePathScope().let(scope)
    }

    /**
     * Save [file].
     *
     * @param file The file you want to save.
     */
    @JvmStatic
    @Deprecated("Using getOrSaveFile instead.", ReplaceWith("getOrSaveFile(dirs)"))
    fun saveFile(file: File): ResultCompat<String> = try {
        if (file.createNewFile()) {
            ResultCompat.success("${file.name} saved successfully")
        } else {
            ResultCompat.failure(IllegalArgumentException("${file.name} is already exists."))
        }
    } catch (e: IOException) {
        ResultCompat.failure(e)
    }

    /**
     * If the [file] exists, get it directly, otherwise try to create it and
     * get it.
     *
     * @since 1.5.1
     */
    @JvmStatic
    fun getOrSaveFile(file: File): ResultCompat<File> = try {
        if (file.exists() || file.createNewFile()) {
            ResultCompat.success(file)
        } else {
            ResultCompat.failure(RuntimeException("${file.name} doesn't exist and failed to create."))
        }
    } catch (e: Exception) {
        ResultCompat.failure(Exception("Failed to get ${file.name}."))
    }

    /**
     * Delete [file].
     *
     * @param file the file you want to delete.
     */
    @JvmStatic
    fun deleteFile(file: File): ResultCompat<String> = if (file.isFile) {
        if (file.delete()) {
            ResultCompat.success("${file.name} successfully deleted.")
        } else {
            ResultCompat.failure(RuntimeException("${file.name} delete failed."))
        }
    } else {
        ResultCompat.failure(IllegalArgumentException("${file.name} is not a normal file or not exists."))
    }

    /**
     * Copy the file to the specified location, this method will not delete the
     * original file.
     *
     * @param from File original location.
     * @param to File copy destination.
     * @since 0.4.0
     */
    @JvmStatic
    fun copyFile(from: File, to: File): ResultCompat<String> = when {
        !from.exists() ->
            ResultCompat.failure(IllegalArgumentException("${from.name} is not exists."))

        getExtension(from) != getExtension(to) ->
            ResultCompat.failure(IllegalArgumentException("The file extensions are inconsistent."))

        else -> {
            val result = saveFile(to)
            if (result.isFailure) {
                result
            } else {
                val input = FileInputStream(from)
                val out = FileOutputStream(to)
                val bt = ByteArray(1024)
                var length: Int
                while (input.read(bt).also { length = it } > 0) {
                    out.write(bt, 0, length)
                }
                input.close()
                out.close()
                ResultCompat.success("File ${from.name} successfully copied to file ${to.name}.")
            }
        }
    }

    /**
     * Move the file to the specified location.
     *
     * @param file File to be moved.
     * @param destination File move destination path. For example:
     * appInternalFilesDir().path.
     * @since 0.4.0
     */
    @JvmStatic
    fun moveFile(file: File, destination: String): ResultCompat<String> = if (!file.exists()) {
        ResultCompat.failure(IllegalArgumentException("${file.name} is not exists."))
    } else {
        val new = File(destination, file.name)
        val saveResult = saveFile(new)
        if (saveResult.isSuccess) {
            val input = FileInputStream(file)
            val out = FileOutputStream(new)
            val byteArray = ByteArray(1024)
            var length: Int
            while (input.read(byteArray).also { length = it } > 0) {
                out.write(byteArray, 0, length)
            }
            input.close()
            out.close()
            val deleteResult = deleteFile(file)
            if (deleteResult.isSuccess) {
                ResultCompat.success("File ${file.name} successfully move to file ${new.path}.")
            } else deleteResult
        } else saveResult
    }

    /**
     * Make directory.
     *
     * @param dir The file of the directory.
     */
    @JvmStatic
    @Deprecated("Using getOrMakeDir instead.", ReplaceWith("getOrMakeDir(dirs)"))
    fun makeDir(dir: File): ResultCompat<String> = when {
        dir.exists() ->
            ResultCompat.failure(IllegalArgumentException("${dir.name} is already exists."))

        dir.mkdir() ->
            ResultCompat.success("${dir.name} is created.")

        else ->
            ResultCompat.failure(RuntimeException("${dir.name} create failed."))
    }

    /**
     * Creates the directory named by this abstract pathname, including any
     * necessary but nonexistent parent directories.
     *
     * @since 1.5.0
     */
    @JvmStatic
    @Deprecated("Using getOrMakeDir instead.", ReplaceWith("getOrMakeDir(dirs)"))
    fun makeDirs(dirs: File): ResultCompat<String> = when {
        dirs.exists() ->
            ResultCompat.failure(IllegalArgumentException("${dirs.name} is already exists."))

        dirs.mkdirs() ->
            ResultCompat.success("${dirs.name} is created.")

        else ->
            ResultCompat.failure(RuntimeException("${dirs.name} create failed."))
    }

    /**
     * If the [dir] exists, get it directly, otherwise Create the directory
     * named by this abstract pathname, including any necessary but nonexistent
     * parent directories.
     *
     * @since 1.5.1
     */
    @JvmStatic
    fun getOrMakeDir(dir: File): ResultCompat<File> = when {
        dir.exists() && dir.isDirectory -> ResultCompat.success(dir)

        dir.exists() && !dir.isDirectory ->
            ResultCompat.failure(IllegalArgumentException("${dir.name} is not a directory."))

        dir.mkdirs() -> ResultCompat.success(dir)

        else -> ResultCompat
            .failure(RuntimeException("${dir.name} doesn't exist and failed to create."))
    }

    /**
     * Delete directory.
     *
     * @param dir The directory you want to delete.
     * @return Operations result.
     */
    @JvmStatic
    fun deleteDir(dir: File): ResultCompat<String> {
        when {
            !dir.exists() ->
                return ResultCompat.failure(IllegalArgumentException("${dir.name} is not exists."))

            null == dir.listFiles() ->
                dir.delete()

            else -> {
                for (f in dir.listFiles()!!) {
                    if (f.isFile) {
                        f.delete()
                    } else if (f.isDirectory) {
                        deleteDir(f)
                    }
                }
            }
        }
        dir.delete()
        return ResultCompat.success("${dir.name} delete successfully.")
    }

    /**
     * Copy the folder to the specified location, this method will not delete
     * the original folder.
     *
     * @param from Folder original location.
     * @param to Folder copy destination.
     * @since 0.4.0
     */
    @JvmStatic
    fun copyDir(from: File, to: File): ResultCompat<String> = if (from.exists()) {
        val files = from.listFiles()
        val makeResult = makeDir(to)
        if (makeResult.isSuccess) {
            files?.forEach { file ->
                if (file.isDirectory) {
                    copyDir(file, File(to.path, file.name))
                } else {
                    copyFile(file, File(to.path, file.name))
                }
            }
            ResultCompat.success("${from.name} copy successful.")
        } else {
            makeResult
        }
    } else {
        ResultCompat.failure(IllegalArgumentException("${from.name} is not exists."))
    }

    /**
     * Move the folder to the specified location.
     *
     * @param dir Folder to be moved.
     * @param destination Folder move destination path.
     * @since 0.4.0
     */
    @JvmStatic
    fun moveDir(dir: File, destination: String): ResultCompat<String> = if (dir.exists()) {
        val files = dir.listFiles()
        val makeResult = makeDir(File(destination))
        if (makeResult.isSuccess) {
            files?.forEach { file ->
                if (file.isDirectory) {
                    copyDir(file, File(destination, file.name))
                } else {
                    val copyResult = copyFile(file, File(destination, file.name))
                    if (copyResult.isSuccess) {
                        deleteFile(file)
                    } else {
                        return copyResult
                    }
                }
                deleteDir(file)
            }
            ResultCompat.success("${dir.name} successful move to $destination.")
        } else {
            makeResult
        }
    } else {
        ResultCompat.failure(IllegalArgumentException("${dir.name} is not exists."))
    }

    /**
     * Renaming file or directory.
     *
     * @param file The file or directory you want to rename.
     * @param newName The new name.
     * @return Operations result.
     */
    @JvmStatic
    fun rename(file: File, newName: String): ResultCompat<String> = when {
        !file.exists() ->
            ResultCompat.failure(IllegalArgumentException("${file.name} is not exists."))

        newName == file.name ->
            ResultCompat.success("${file.name} rename successfully.")

        null == file.parent ->
            ResultCompat.failure(RuntimeException("${file.name} parent is null."))

        else -> {
            val newFile = File(file.parent, newName)
            if (!newFile.exists() && file.renameTo(newFile)) {
                ResultCompat.success("${file.name} rename successfully.")
            } else {
                ResultCompat.failure(RuntimeException("${file.name} rename failed."))
            }
        }
    }

    /**
     * Get encrypted file.
     *
     * @param file
     */
    @JvmStatic
    fun getEncryptedFile(file: File): EncryptedFile {
        return EncryptedFile.Builder(
            ContextHelper.getAppContext(),
            file,
            ToolsConfig.getMasterKey(),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    /**
     * Get assets cache file.
     *
     * @param fileName The assets file name.
     * @param dir The dir where the file saved.
     * @since 0.5.1
     */
    @JvmStatic
    fun getAssetsFile(fileName: String, dir: File = appInternalFilesDir()): File {
        val saveFile = File(dir, fileName)
        try {
            ContextHelper.getAppContext().assets.open(fileName).use { inputStream ->
                FileOutputStream(saveFile).use { outputStream ->
                    val buf = ByteArray(1024)
                    var len: Int
                    while (inputStream.read(buf).also { len = it } > 0) {
                        outputStream.write(buf, 0, len)
                    }
                }
            }
            return saveFile
        } catch (exception: Exception) {
            throw exception
        }
    }

    /**
     * Checks if a volume containing external storage is available for read and
     * write.
     *
     * @since 0.5.1
     */
    @JvmStatic
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * Checks if a volume containing external storage is available to at least
     * read.
     *
     * @since 0.5.1
     */
    @JvmStatic
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    /** Get the extension name of the file. */
    @JvmStatic
    fun getExtension(file: File): String {
        return MimeTypeMap.getFileExtensionFromUrl(file.toString())
    }

    /**
     * Get the mimetype of the [file], [fallback] otherwise. For more
     * information, please refer to
     * [How to determine MIME type of file in android?](https://stackoverflow.com/questions/8589645/how-to-determine-mime-type-of-file-in-android)
     */
    @JvmStatic
    fun getMimeType(file: File, fallback: String): String {
        return MimeTypeMap.getFileExtensionFromUrl(file.toString())
            ?.run { mimeTypeMap.getMimeTypeFromExtension(lowercase(Locale.ROOT)) }
            ?: fallback
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
     * @since 0.5.0
     */
    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.N)
    fun getFileUriAboveApi24(file: File, authority: String): Uri {
        return FileProvider.getUriForFile(ContextHelper.getAppContext(), authority, file)
    }

    /**
     * Get the uri by [file].
     *
     * @since 0.5.0
     */
    @JvmStatic
    fun getFileUriOnApi23(file: File): Uri {
        return Uri.fromFile(file)
    }

}