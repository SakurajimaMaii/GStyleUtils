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

package com.ave.vastgui.app.activity.view

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.core.database.getStringOrNull
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.ContactAdapter
import com.ave.vastgui.app.adapter.entity.Contact
import com.ave.vastgui.app.databinding.ActivityAlphabetSidebarBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.permission.requestPermission
import com.ave.vastgui.tools.view.alphabetsidebar.Alphabet
import com.ave.vastgui.tools.view.alphabetsidebar.AlphabetSideBar
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/28
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/alphabetsidebar/alphabetsidebar/
// Documentation: https://ave.entropy2020.cn/documents/adapter/

class AlphabetSideBarActivity : ComponentActivity(R.layout.activity_alphabet_sidebar) {

    /**
     * SmoothScrollLayoutManager
     *
     * [Smooth-scroll-layout-manager](https://juejin.cn/post/6844903504310435853)
     */
    private class SmoothScrollLayoutManager(context: Context) : LinearLayoutManager(context) {
        override fun smoothScrollToPosition(
            recyclerView: RecyclerView,
            state: RecyclerView.State,
            position: Int
        ) {
            val smoothScroller: LinearSmoothScroller =
                object : LinearSmoothScroller(recyclerView.context) {
                    // 返回：滑过1px时经历的时间(ms)。
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return 0.08f
                    }

                    // https://blog.csdn.net/qq_16251833/article/details/81220518
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }

    private val mBinding: ActivityAlphabetSidebarBinding
            by viewBinding(ActivityAlphabetSidebarBinding::bind)
    private val mAdapter by lazy { ContactAdapter(this) }
    private var mSmoothScrollLayoutManager by NotNUllVar<SmoothScrollLayoutManager>()
    private val mLogger = logFactory.getLogCat(AlphabetSideBarActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission(Manifest.permission.READ_CONTACTS) {
            granted = {
                mLogger.i("已获取读取通讯录权限")
            }
        }

        mSmoothScrollLayoutManager = SmoothScrollLayoutManager(this)
        mBinding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = mSmoothScrollLayoutManager
        }

        mBinding.alphabetsidebar.refreshWithInvalidate {
            mBackgroundColor =
                ColorUtils.getColorIntWithTransparency(15, ColorUtils.colorHex2Int("#b2bec3"))
        }
        mBinding.alphabetsidebar.setLetterListener(object : AlphabetSideBar.LetterListener {
            override fun onIndicatorLetterUpdate(letter: String, index: Int, target: Int) {
                if (-1 != target) {
                    mBinding.recyclerView.smoothScrollToPosition(target)
                }
            }

            override fun onIndicatorLetterTargetUpdate(letter: String, target: Int) {
                mLogger.d("$letter 目标索引更新为 $target")
            }
        })

        lifecycleScope.launch {
            mAdapter.add(readContacts(), R.layout.item_contact) {
                addOnItemChildClickListener(R.id.name) { _, _, contact ->
                    SimpleToast.showShortMsg("名字是 ${contact?.name}")
                }
                addOnItemChildClickListener(R.id.number) { _, _, contact ->
                    SimpleToast.showShortMsg("电话号码是 ${contact?.number}")
                }
            }
            updateIndex()
        }
    }

    /** 检查数据库是否包含 **phonebook_label** 字段， 如果没有则用 [Phone.SORT_KEY_PRIMARY] 替代。 */
    private fun checkPhonebookLabel(): String {
        var cursor: Cursor? = null
        var key = "phonebook_label"
        try {
            cursor = contentResolver
                .query(Phone.CONTENT_URI, arrayOf("phonebook_label"), null, null, null)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
            key = Phone.SORT_KEY_PRIMARY
        } finally {
            cursor?.close()
        }
        return key
    }

    /** 按照名字首字母读取通讯录。 */
    private suspend fun readContacts(): List<Contact> = withContext(Dispatchers.IO) {
        val contacts = mutableListOf<Contact>()
        val bookLabel = checkPhonebookLabel()
        val projection = arrayOf(Phone.DISPLAY_NAME, Phone.NUMBER, bookLabel)
        contentResolver
            .query(Phone.CONTENT_URI, projection, null, null, bookLabel)
            ?.use { cursor ->
                while (cursor.moveToNext()) {
                    val name: String =
                        cursor.getStringOrNull(cursor.getColumnIndex(Phone.DISPLAY_NAME)).toString()
                    val number: String =
                        cursor.getStringOrNull(cursor.getColumnIndex(Phone.NUMBER)).toString()
                    val label: String =
                        cursor.getStringOrNull(cursor.getColumnIndex(bookLabel)).toString()
                    contacts.add(Contact(name, number, label))
                }
            }
        return@withContext contacts
    }

    /** 根据获取到的通讯录列表更新索引值。 */
    private fun updateIndex() {
        enumValues<Alphabet>().forEach { alphabet ->
            val index = mAdapter.data.indexOfFirst {
                it.label == alphabet.letter
            }
            mBinding.alphabetsidebar.setIndicatorLetterTargetIndex(alphabet, index)
        }
    }

}