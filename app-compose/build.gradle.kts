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
    kotlin("android")
    kotlin("plugin.serialization")
    id("com.android.application")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.ave.vastgui.appcompose"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ave.vastgui.appcompose"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
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
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
}

dependencies {
    debugImplementation(libs.ui.test.manifest.compose)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.activity.compose)
    implementation(libs.adapter.rxjava3)
    implementation(libs.coil.compose)
    implementation(libs.converter.gson)
    implementation(libs.core.ktx)
    implementation(libs.datastore.preferences)
    implementation(libs.fragment.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.material.compose)
    implementation(libs.material3.compose)
    implementation(libs.okhttp)
    implementation(libs.paging.compose)
    implementation(libs.retrofit)
    implementation(libs.runtime.livedata.compose)
    implementation(libs.slf4j.api)
    implementation(libs.ui.compose)
    implementation(libs.ui.graphics.compose)
    implementation(libs.ui.tooling.preview.compose)
    implementation(projects.libraries.kernel)
    implementation(projects.libraries.log.core)
    implementation(projects.libraries.log.slf4j)
    implementation(projects.libraries.tools)
}