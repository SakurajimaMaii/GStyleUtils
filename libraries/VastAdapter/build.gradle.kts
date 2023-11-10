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

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("convention.publication")
}

android {
    namespace = "com.ave.vastgui.adapter"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        freeCompilerArgs = listOf("-Xjvm-default=all-compatibility")
    }

    buildFeatures {
        dataBinding = true
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    api(libs.paging.runtime)
    api(libs.recyclerview)
    api(libs.vastcore)
    implementation(libs.annotation)
    implementation(libs.core.ktx)
    implementation(libs.core.runtime)
}

extra["PUBLISH_ARTIFACT_ID"] = "VastAdapter"
extra["PUBLISH_DESCRIPTION"] = "Help you quickly build an Adapter suitable for RecyclerView."
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/VastAdapter"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sakurajimamaii"
            artifactId = "VastAdapter"
            version = "0.1.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}