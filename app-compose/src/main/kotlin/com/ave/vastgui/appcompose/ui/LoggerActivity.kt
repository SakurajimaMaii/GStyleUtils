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

package com.ave.vastgui.appcompose.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/12

class LoggerActivity : ComponentActivity() {

    private val mLogger = LoggerFactory.getLogger("LoggerActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }

    override fun onResume() {
        super.onResume()
        dSimpleUsage()
    }

    private fun dSimpleUsage() {
        val marker = MarkerFactory.getMarker("Marker")
        if (false) {
            mLogger.debug(marker, HELLO_WORLD)
            mLogger.debug(marker, HELLO_WORLD_FORMAT_1, HELLO_WORLD)
            mLogger.debug(marker, HELLO_WORLD_FORMAT_2, HELLO_WORLD, HELLO_WORLD)
            mLogger.debug(marker, HELLO_WORLD_FORMAT_MORE, HELLO_WORLD, HELLO_WORLD, HELLO_WORLD)
            mLogger.debug(marker, HELLO_WORLD, Exception(HELLO_WORLD_EXCEPTION))
            mLogger.debug(HELLO_WORLD)
            mLogger.debug(HELLO_WORLD_FORMAT_1, HELLO_WORLD)
            mLogger.debug(HELLO_WORLD_FORMAT_2, HELLO_WORLD, HELLO_WORLD)
            mLogger.debug(HELLO_WORLD_FORMAT_MORE, HELLO_WORLD, HELLO_WORLD, HELLO_WORLD)
            mLogger.debug(HELLO_WORLD, Exception(HELLO_WORLD_EXCEPTION))
        }
        mLogger.atDebug().log {
            Throwable().printStackTrace()
            HELLO_WORLD
        }
    }

    companion object {
        private const val HELLO_WORLD_EXCEPTION: String = "Hello World Exception."
        private const val HELLO_WORLD: String = "Hello World"
        private const val HELLO_WORLD_FORMAT_1: String = "Print 1#{}."
        private const val HELLO_WORLD_FORMAT_2: String = "Print 1#{} 2#{}."
        private const val HELLO_WORLD_FORMAT_MORE: String = "Print 1#{} 2#{} 3#{}."
    }

}