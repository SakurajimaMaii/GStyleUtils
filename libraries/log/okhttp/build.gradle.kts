/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.net.URL

plugins {
    kotlin("jvm")
    id("java-library")
    id("convention.publication")
}

group = "io.github.sakurajimamaii"
version = "1.3.9"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

tasks.named<KotlinJvmCompile>("compileKotlin") {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

sourceSets["main"].java.srcDir("src/main/kotlin")

kotlin.sourceSets.all {
    languageSettings.optIn("com.log.vastgui.core.annotation.LogApi")
}

dependencies{
    compileOnly(projects.libraries.kernel)
    compileOnly(projects.libraries.log.core)
    implementation(libs.okhttp)
    implementation(libs.okhttp.sse)
    testImplementation(libs.fastjson2)
    testImplementation(libs.gson)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
    testImplementation(libs.ktor.client.logging)
    testImplementation(projects.libraries.kernel)
    testImplementation(projects.libraries.log.core)
}

extra["PUBLISH_ARTIFACT_ID"] = "log-okhttp"
extra["PUBLISH_DESCRIPTION"] = "Log for okhttp"
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/log/okhttp"

val mavenPropertiesFile = File(rootDir, "maven.properties")
if (mavenPropertiesFile.exists()) {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "io.github.sakurajimamaii"
                artifactId = "log-okhttp"
                version = "1.3.9"

                afterEvaluate {
                    from(components["java"])
                }
            }
        }
    }
}

tasks.withType<DokkaTaskPartial> {
    moduleName.set("log-okhttp")
    dokkaSourceSets.configureEach {
        sourceLink {
            // FIXME https://github.com/Kotlin/dokka/issues/2876
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl.set(URL("https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/log/okhttp/src"))
            remoteLineSuffix.set("#L")
        }
    }
}