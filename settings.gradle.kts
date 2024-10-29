/*
 * Copyright 2022 VastGui guihy2019@gmail.com
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

include(":app")
include(":app-skin")
include(":app-compose")
includeBuild("convention-plugins")

rootProject.name = "Android-Vast-Extension"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// =======
// = Lib =
// =======
val libs = arrayOf(
    "adapter",
    "kernel",
    "log:android",
    "log:core",
    "log:desktop",
    "log:mars",
    "log:okhttp",
    "log:slf4j",
    "netstatelayout",
    "tools",
)

libs.forEach {
    include(":libraries:$it")
}

// =======
// = Dev =
// =======
val devenv = arrayOf(
    "streamapp"
)

devenv.forEach {
    include(":devenv:$it")
}
