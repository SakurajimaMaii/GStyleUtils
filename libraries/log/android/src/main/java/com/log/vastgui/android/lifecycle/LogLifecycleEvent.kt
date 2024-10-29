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
import androidx.fragment.app.Fragment

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/18
// Documentation: https://ave.entropy2020.cn/documents/log/log-android/lifecycle/usgae/
// Reference: https://github.com/Chesire/LifecykleLog

/**
 * Contains a list of lifecycle events available on a [Fragment] or
 * [Activity]. Use [eventName] to get its string representation.
 *
 * @since 1.3.10
 */
enum class LogLifecycleEvent(val eventName: String) {
    ON_ATTACH("onAttach"),
    ON_CREATE("onCreate"),
    ON_CREATE_VIEW("onCreateView"),
    ON_ACTIVITY_CREATED("onActivityCreated"),
    ON_START("onStart"),
    ON_RESUME("onResume"),
    ON_PAUSE("onPause"),
    ON_STOP("onStop"),
    ON_DESTROY_VIEW("onDestroyView"),
    ON_DESTROY("onDestroy"),
    ON_DETACH("onDetach"),
    ON_PRE_ATTACHED("onPreAttached"),
    ON_PRE_CREATED("onPreCreated"),
    ON_SAVE_INSTANCE_STATE("onSaveInstanceState")
}