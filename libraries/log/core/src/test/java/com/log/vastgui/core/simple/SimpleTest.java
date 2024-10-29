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

package com.log.vastgui.core.simple;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/11
// Documentation: 
// Reference:

import com.log.vastgui.core.Factory;
import com.log.vastgui.core.LogCat;
import com.log.vastgui.core.base.LogTag;

import org.junit.Test;

public class SimpleTest {

    private static final String TAG = "SimpleTest";
    private static final String HELLO_WORLD = "Hello World.";
    private final LogCat logcat = Factory.getLogFactory().invoke("SimpleTest");

    @Test
    public void usage() {
        System.out.println(HELLO_WORLD);
    }

    @Test
    public void simpleUsage() {
        LogTag tag = new LogTag(TAG);
        logcat.e(tag, HELLO_WORLD);
        logcat.e(tag, this::content);
        logcat.e(tag, HELLO_WORLD, new Exception(HELLO_WORLD));
        logcat.e(tag, new Exception(HELLO_WORLD), this::content);
        logcat.e(tag, new Exception(HELLO_WORLD));
        logcat.e(HELLO_WORLD);
        logcat.e(this::content);
        logcat.e(HELLO_WORLD, new Exception(HELLO_WORLD));
        logcat.e(new Exception(HELLO_WORLD), this::content);
        logcat.e(new Exception(HELLO_WORLD));
    }

    public void objectUsage() {

    }

    private String content() {
        return HELLO_WORLD;
    }

}
