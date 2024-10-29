import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

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

plugins {
    kotlin("android")
    id("com.android.library")
    id("convention.publication")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.ave.vastgui.netstatelayout"
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
        freeCompilerArgs = listOf("-Xjvm-default=all")
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
    implementation(libs.material)
}

extra["PUBLISH_ARTIFACT_ID"] = "VastNetStateLayout"
extra["PUBLISH_DESCRIPTION"] = "A layout to set customized network state page."
extra["PUBLISH_URL"] =
    "https://github.com/SakurajimaMaii/Android-Vast-Extension"

val mavenPropertiesFile = File(rootDir, "maven.properties")
if (mavenPropertiesFile.exists()) {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "io.github.sakurajimamaii"
                artifactId = "VastNetStateLayout"
                version = "1.1.2"

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
            remoteUrl.set(URL("https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/netstatelayout/src"))
            remoteLineSuffix.set("#L")
        }
    }
}