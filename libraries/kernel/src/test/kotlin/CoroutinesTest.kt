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

import com.ave.vastgui.core.coroutines.suspendCoroutineWithTimeout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.coroutines.resumeWithException
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

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
// Date: 2024/11/13

/**
 * In general, you should have one invocation of runTest per test, and
 * using an expression body is recommended.
 *
 * Wrapping your test’s code in runTest will work for testing basic
 * suspending functions, and it will automatically skip any delays in
 * coroutines, making the test above complete much faster than one second.
 *
 * However, there are additional considerations to make, depending on what
 * happens in your code under test:
 * - When your code creates new coroutines other than the top-level test
 *   coroutine that runTest creates, you’ll need to control how those new
 *   coroutines are scheduled by choosing the appropriate TestDispatcher.
 * - If your code moves the coroutine execution to other dispatchers (for
 *   example, by using withContext), runTest will still generally work, but
 *   delays will no longer be skipped, and tests will be less predictable as
 *   code runs on multiple threads. For these reasons, in tests you should
 *   inject test dispatchers to replace real dispatchers.
 */
class CoroutinesTest {

    @Test
    fun cancellableTimeout() = runTest {
        val result = withContext(Dispatchers.IO) {
            suspendCoroutineWithTimeout(7000L) { coroutine ->
                simulateRequest(object : Callback<String> {
                    override fun onSuccess(t: String) {
                        coroutine.resumeWith(Result.success(t))
                    }

                    override fun onFailed(e: Exception) {
                        coroutine.resumeWithException(e)
                    }
                })
            }
        }
        assertEquals(RESPONSE_STRING, result)
    }

    private fun simulateRequest(result: Callback<String>) {
        thread {
            Thread.sleep(5000)
            if (Random(System.currentTimeMillis()).nextLong(0, 100) > 0) {
                result.onSuccess(RESPONSE_STRING)
            } else {
                result.onFailed(Exception("FAILED"))
            }
        }
    }

    interface Callback<T> {
        fun onSuccess(t: T)
        fun onFailed(e: Exception)
    }

    companion object {
        private const val RESPONSE_STRING = "Hello World"
    }

}