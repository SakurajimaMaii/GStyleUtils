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
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization")
}

android {
    compileSdkPreview = "UpsideDownCake"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ave.vastgui.app"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk { abiFilters.addAll(arrayOf("armeabi-v7a", "arm64-v8a")) }
    }

    buildTypes {

        release {
            isDebuggable = false
            isJniDebuggable = false
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            kotlin {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/release/kotlin")
                }
            }
        }

        debug {
            isDebuggable = true
            isJniDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            kotlin {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/debug/kotlin")
                }
            }
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    namespace = "com.ave.vastgui.app"

    sourceSets["main"].java.srcDirs("src/main/kotlin", "src/main/java")
}

kotlin.sourceSets.all {
    languageSettings.optIn("com.log.vastgui.core.annotation.LogApi")
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.adapter.rxjava3)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.coil)
    implementation(libs.constraintlayout)
    implementation(libs.converter.gson)
    implementation(libs.core.splashscreen)
    implementation(libs.dialogx)
    implementation(libs.dialogxiosstyle)
    implementation(libs.fragment.ktx)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.rx3)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.material)
    implementation(libs.okhttp)
    implementation(libs.paging.runtime)
    implementation(libs.paintedskin)
    implementation(libs.paintedskin.constraintlayout.compat)
    implementation(libs.paintedskin.standard.plugin)
    implementation(libs.reflex)
    implementation(libs.retrofit)
    implementation(libs.security.crypto)
    implementation(projects.libraries.adapter)
    implementation(projects.libraries.kernel)
    implementation(projects.libraries.log.mars)
    implementation(projects.libraries.log.okhttp)
    implementation(projects.libraries.log.android)
    implementation(projects.libraries.netstatelayout)
    implementation(projects.libraries.tools)
}