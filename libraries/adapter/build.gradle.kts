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
    kotlin("kapt")
    kotlin("android")
    id("com.android.library")
    id("convention.publication")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.ave.vastgui.adapter"
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
        freeCompilerArgs += listOf("-Xjvm-default=all")
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
    implementation(libs.paging.runtime)
    implementation(libs.recyclerview)
    implementation(libs.core.ktx)
}

extra["PUBLISH_ARTIFACT_ID"] = "VastAdapter"
extra["PUBLISH_DESCRIPTION"] = "Quickly Adapter for RecyclerView."
extra["PUBLISH_URL"] =
    "https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/adapter"

val mavenPropertiesFile = File(rootDir, "maven.properties")
if (mavenPropertiesFile.exists()) {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "io.github.sakurajimamaii"
                artifactId = "VastAdapter"
                version = "1.2.0"

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
            remoteUrl.set(URL("https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/adapter/src"))
            remoteLineSuffix.set("#L")
        }
    }
}