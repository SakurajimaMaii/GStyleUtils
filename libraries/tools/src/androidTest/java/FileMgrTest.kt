
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ave.vastgui.tools.manager.filemgr.FileMgr.getOrMakeDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.getOrSaveFile
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class FileMgrTest {

    @Test
    fun getOrMakeDir() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dir = getOrMakeDir(File(appContext.filesDir, "test"))
            .getOrNull()
        Assert.assertTrue(null != dir && dir.isDirectory)
    }

    @Test
    fun getOrSaveFile() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val txt = getOrSaveFile(File(appContext.filesDir, "test.txt"))
            .getOrNull()
        Assert.assertTrue(null != txt && txt.isFile)
    }

}