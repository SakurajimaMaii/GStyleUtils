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

import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

plugins {
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("convention.publication")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.ave.vastgui.tools"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        // https://stackoverflow.com/questions/53964192/jvmdefault-and-how-add-compiler-option
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }

    viewBinding.isEnabled = true

    // https://www.jianshu.com/p/2a539b497460
    // https://www.lordcodes.com/tips/kotlin-source-directory-gradle-kotlin-dsl
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("com.log.vastgui.core.annotation.LogApi")
}

dependencies {
    api(libs.security.crypto)
    api(libs.zxing.core)
    api(projects.libraries.log.core)
    implementation(libs.activity.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.versionedparcelable)
    implementation(libs.annotation)
    implementation(libs.appcompat)
    implementation(libs.appcompat.resources)
    implementation(libs.constraintlayout)
    implementation(libs.converter.gson)
    implementation(libs.core.ktx)
    implementation(libs.core.runtime)
    implementation(libs.datastore.preferences)
    implementation(libs.exifinterface)
    implementation(libs.fragment.ktx)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.lifecycle.livedata.core.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.material)
    implementation(libs.monitor)
    implementation(libs.okhttp)
    implementation(libs.recyclerview)
    implementation(libs.retrofit)
    implementation(projects.libraries.kernel)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.junit)
}

extra["PUBLISH_ARTIFACT_ID"] = "VastTools"
extra["PUBLISH_DESCRIPTION"] = "Easy Quick Android Tools for you to faster project development."
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/Android-Vast-Extension"

val mavenPropertiesFile = File(rootDir, "maven.properties")
if (mavenPropertiesFile.exists()) {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "io.github.sakurajimamaii"
                artifactId = "VastTools"
                version = "1.5.1"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}

tasks.withType<DokkaTaskPartial> {
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl.set(URL("https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/tools/src"))
            remoteLineSuffix.set("#L")
        }
    }
}