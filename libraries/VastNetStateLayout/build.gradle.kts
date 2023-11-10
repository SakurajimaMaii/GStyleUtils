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

import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("convention.publication")
    id("org.jetbrains.dokka")
}

android {
    compileSdk = 34
    namespace = "com.ave.vastgui.netstatelayout"

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner ="androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.annotation)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        jdkVersion.set(17)
        languageVersion.set("1.9.0")
        suppressInheritedMembers.set(true)
        documentedVisibilities.set(
            setOf(Visibility.PUBLIC, Visibility.PROTECTED, Visibility.PRIVATE, Visibility.INTERNAL)
        )
    }
}

extra["PUBLISH_ARTIFACT_ID"] = "VastNetStateLayout"
extra["PUBLISH_DESCRIPTION"] = "A layout to set customized network state ui."
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/VastUtils/tree/master/libraries/VastNetStateLayout"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sakurajimamaii"
            artifactId = "VastNetStateLayout"
            version = "0.0.4"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}