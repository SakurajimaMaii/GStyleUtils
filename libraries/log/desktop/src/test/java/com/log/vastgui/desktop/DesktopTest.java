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

package com.log.vastgui.desktop;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/15 11:44

import static com.log.vastgui.desktop.LogFactoryKt.logFactory;

import com.log.vastgui.core.LogCat;

import org.junit.Test;

public class DesktopTest {

    private static final LogCat logcat = logFactory.invoke("DesktopTest");

    @Test
    public void logTest() {
        logcat.v("这是一条测试日志");
        logcat.d("这是一条测试日志");
        logcat.i("这是一条测试日志");
        logcat.w("这是一条测试日志");
        logcat.e("这是一条测试日志");
    }

}