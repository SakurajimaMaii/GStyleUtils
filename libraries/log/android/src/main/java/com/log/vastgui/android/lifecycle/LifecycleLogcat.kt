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

package com.log.vastgui.android.lifecycle

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/7/17
// Documentation: https://ave.entropy2020.cn/documents/log/log-android/lifecycle/usgae/
// Reference: https://github.com/Chesire/LifecykleLog

/**
 * Handles callbacks for all of the [Activity] lifecycle events. When
 * [onActivityCreated] is called, it will register for the [Fragment]
 * lifecycle events.
 *
 * @since 1.3.10
 */
class LifecycleLogcat private constructor(private val observer: LifecycleCallback) :
    ActivityLifecycleCallbacks, FragmentLifecycleCallbacks() {

    /** @since 1.3.10 */
    private val eventOwnerMap: ConcurrentMap<Class<*>, Pair<String, Array<LogLifecycleEvent>>> =
        ConcurrentHashMap()

    // region Activity lifecycle observer
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!activity.register()) return
        val pair = activity.pair(LogLifecycleEvent.ON_CREATE) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_CREATE, savedInstanceState)
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(this, true)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        val pair = activity.pair(LogLifecycleEvent.ON_START) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_START, null)
    }

    override fun onActivityResumed(activity: Activity) {
        val pair = activity.pair(LogLifecycleEvent.ON_RESUME) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_RESUME, null)
    }

    override fun onActivityPaused(activity: Activity) {
        val pair = activity.pair(LogLifecycleEvent.ON_PAUSE) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_PAUSE, null)
    }

    override fun onActivityStopped(activity: Activity) {
        val pair = activity.pair(LogLifecycleEvent.ON_STOP) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_STOP, null)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        val pair = activity.pair(LogLifecycleEvent.ON_SAVE_INSTANCE_STATE) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_SAVE_INSTANCE_STATE, outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        val pair = activity.pair(LogLifecycleEvent.ON_DESTROY) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_DESTROY, null)
        activity.unregister()
    }
    // endregion

    // region Fragment lifecycle observer
    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        if (!f.register()) return
        val pair = f.pair(LogLifecycleEvent.ON_PRE_ATTACHED) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_PRE_ATTACHED, null)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        val pair = f.pair(LogLifecycleEvent.ON_ATTACH) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_ATTACH, null)
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        val pair = f.pair(LogLifecycleEvent.ON_PRE_CREATED) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_PRE_CREATED, null)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        val pair = f.pair(LogLifecycleEvent.ON_CREATE) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_CREATE, null)
    }

    @Deprecated("Deprecated in Java")
    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        val pair = f.pair(LogLifecycleEvent.ON_ACTIVITY_CREATED) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_ACTIVITY_CREATED, null)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        val pair = f.pair(LogLifecycleEvent.ON_CREATE_VIEW) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_CREATE_VIEW, null)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        val pair = f.pair(LogLifecycleEvent.ON_START) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_START, null)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val pair = f.pair(LogLifecycleEvent.ON_RESUME) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_RESUME, null)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        val pair = f.pair(LogLifecycleEvent.ON_PAUSE) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_PAUSE, null)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        val pair = f.pair(LogLifecycleEvent.ON_STOP) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_STOP, null)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        val pair = f.pair(LogLifecycleEvent.ON_SAVE_INSTANCE_STATE) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_SAVE_INSTANCE_STATE, null)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        val pair = f.pair(LogLifecycleEvent.ON_DESTROY) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_DESTROY, null)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        val pair = f.pair(LogLifecycleEvent.ON_DESTROY_VIEW) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_DESTROY_VIEW, null)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        val pair = f.pair(LogLifecycleEvent.ON_DETACH) ?: return
        observer.observeEvent(pair.first, LogLifecycleEvent.ON_DETACH, null)
        f.unregister()
    }
    // endregion

    /** @since 1.3.10 */
    private fun Any.getKey(default: String = "") = default.ifBlank { this::class.java.simpleName }

    /** @since 1.3.10 */
    private fun Any.pair(event: LogLifecycleEvent): Pair<String, Array<LogLifecycleEvent>>? {
        val pair = eventOwnerMap[this::class.java] ?: return null
        return pair.takeIf { it.second.contains(event) }
    }

    /** @since 1.3.10 */
    private fun Any.register(): Boolean {
        val annotation: LogLifecycle =
            this::class.java.getAnnotation(LogLifecycle::class.java) ?: return false
        val name = getKey(annotation.name)
        eventOwnerMap.putIfAbsent(this::class.java, name to annotation.obverseEvent)
        return true
    }

    /** @since 1.3.10 */
    private fun Any.unregister() {
        val annotation: LogLifecycle =
            this::class.java.getAnnotation(LogLifecycle::class.java) ?: return
        val name = getKey(annotation.name)
        eventOwnerMap.remove(this::class.java, name to annotation.obverseEvent)
    }

    /** @since 1.3.10 */
    @FunctionalInterface
    fun interface LifecycleCallback {
        fun observeEvent(tag: String, event: LogLifecycleEvent, bundle: Bundle?)
    }

    companion object {
        /**
         * Register a lifecycle logcat.
         *
         * ```kotlin
         * class App : Application() {
         *     override fun onCreate() {
         *         super.onCreate()
         *         registerLifecycleLogcat { tag, event, bundle ->
         *             // ...
         *         }
         *     }
         * }
         * ```
         *
         * @since 1.3.10
         */
        fun Application.registerLifecycleLogcat(cb: LifecycleCallback) {
            registerActivityLifecycleCallbacks(LifecycleLogcat(cb))
        }
    }
}