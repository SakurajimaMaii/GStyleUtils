import android.media.AudioManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ave.vastgui.tools.media.getAudioManager
import com.ave.vastgui.tools.media.isFeatureAudioOutput
import com.ave.vastgui.tools.utils.isAirplaneMode
import com.ave.vastgui.tools.utils.isLocationEnabled
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/12/31

@RunWith(AndroidJUnit4::class)
class SystemTest {

//    val runtimePermissionRule: GrantPermissionRule
//        @Rule get() = GrantPermissionRule.grant(Manifest.permission.BLUETOOTH_CONNECT)

    @Test
    fun checkAirplane() {
        Assert.assertTrue(isAirplaneMode())
    }

    @Test
    fun checkLocationEnable() {
        Assert.assertTrue(isLocationEnabled())
    }

//    @Test
//    fun checkBleEnabled() {
//        Assert.assertTrue(isBleEnabled())
//    }

    @Test
    fun checkAudio() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        println(getAudioManager().getDevices(AudioManager.GET_DEVICES_OUTPUTS).map { it.type }.joinToString())
        Assert.assertTrue(isFeatureAudioOutput())
    }

}