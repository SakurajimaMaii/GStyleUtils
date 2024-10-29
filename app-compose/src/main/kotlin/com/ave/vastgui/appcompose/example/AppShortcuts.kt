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

package com.ave.vastgui.appcompose.example

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat
import com.ave.vastgui.appcompose.ui.MainActivity
import com.ave.vastgui.appcompose.R
import com.ave.vastgui.tools.utils.shortcut.createPinnedShortcut
import com.ave.vastgui.tools.utils.shortcut.setDynamicShortcuts

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/2
// Documentation: https://ave.entropy2020.cn/documents/tools/app-entry-points/app-shortcuts/

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PinnedAppShortcuts(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(
        onClick = {
            createPinnedShortcut(
                shortcutId = "OnlyId",
                shortcutResultIntent = { intent ->
                    PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                }
            ) {
                return@createPinnedShortcut Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                }
            }
        },
        modifier = modifier
    ) {
        Text(text = "创建一个快捷方式")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DynamicAppShortcuts(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(
        onClick = {
            val shortcut = ShortcutInfoCompat.Builder(context, "OnlyId")
                .setShortLabel("Website")
                .setLongLabel("Open the website")
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_foreground))
                .setIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.mysite.example.com/")
                    )
                )
                .build()
            setDynamicShortcuts(listOf(shortcut))
        },
        modifier = modifier
    ) {
        Text(text = "创建一个动态快捷方式")
    }
}
