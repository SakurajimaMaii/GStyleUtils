import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ave.vastgui.tools.utils.ColorUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

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
// Date: 2024/10/17

@RunWith(AndroidJUnit4::class)
class ColorTest {

    @Test
    fun colorOpacity() {
        for (i in 0..100) {
            val expected = ColorUtils.ColorOpacity[i]
            val actual = ((i / 100f) * 255f).roundToInt().toString(16).padStart(2, '0')
            Assert.assertEquals("$i $expected $actual", expected, actual)
        }
    }

    @Test
    fun colorInt2Hex() {
        val colorHex = "#FF000000"
        val colorInt = Color.parseColor(colorHex)
        Assert.assertEquals(colorHex, ColorUtils.colorInt2Hex(colorInt))
    }

}