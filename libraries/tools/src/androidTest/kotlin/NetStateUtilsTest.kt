import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ave.vastgui.tools.utils.NetStateUtils
import com.ave.vastgui.tools.utils.NetStateUtils.getConnectivityManager
import com.ave.vastgui.tools.utils.NetStateUtils.getWifiManager
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
// Date: 2024/11/5

@RunWith(AndroidJUnit4::class)
class NetStateUtilsTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun getWifiManager() {
        Assert.assertEquals(WifiManager::class.java, context.getWifiManager()::class.java)
    }

    @Test
    fun getConnectivityManager() {
        Assert.assertEquals(ConnectivityManager::class.java, context.getConnectivityManager()::class.java)
    }

    @Test
    fun hasWIFI() {
        val network = NetStateUtils.hasWIFI(context)
        val address = context.getConnectivityManager()
            .getLinkProperties(network)?.linkAddresses ?: emptyList()
        address.forEach { Log.d(TAG, it.address.hostAddress?.toString() ?: "") }
    }

    @Test
    fun hasMobile() {
        Assert.assertEquals(null, NetStateUtils.hasMobile(context, 5000L))
    }

    @Test
    fun hasEtherNet() {
        Assert.assertEquals(null, NetStateUtils.hasEtherNet(context, 5000L))
    }

    @Test
    fun isMobile() {
        Assert.assertTrue(NetStateUtils.isMobile(context))
    }

    @Test
    fun isWIFI() {
        Assert.assertTrue(NetStateUtils.isWIFI(context))
    }

    @Test
    fun isEtherNet() {
        Assert.assertTrue(NetStateUtils.isEtherNet(context))
    }

    companion object {
        const val TAG = "NET_STATE_UTILS"
    }

}