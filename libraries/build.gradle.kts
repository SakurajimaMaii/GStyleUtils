/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

dokka {
    moduleName.set("Android-Vast-Extension")
    // FIXME java.lang.OutOfMemoryError: Java heap space
    // https://github.com/Kotlin/dokka/issues/3885
    dokkaGeneratorIsolation = ClassLoaderIsolation()
    dokkaSourceSets.configureEach {
        jdkVersion.set(17)
        languageVersion.set("2.0.21")
        documentedVisibilities.set(
            setOf(
                VisibilityModifier.Public,
                VisibilityModifier.Protected,
                VisibilityModifier.Private,
                VisibilityModifier.Internal,
            )
        )
    }

    // Update documentation aggregation in multi-module projects
    // https://kotlinlang.org/docs/dokka-migration.html#update-documentation-aggregation-in-multi-module-projects
    dependencies {
        dokka(projects.libraries.adapter)
        dokka(projects.libraries.kernel)
        dokka(projects.libraries.log.android)
        dokka(projects.libraries.log.core)
        dokka(projects.libraries.log.desktop)
        dokka(projects.libraries.log.mars)
        dokka(projects.libraries.log.okhttp)
        dokka(projects.libraries.log.slf4j)
        dokka(projects.libraries.netstatelayout)
        dokka(projects.libraries.tools)
    }
}