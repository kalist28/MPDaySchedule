import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.28.0"
}

group = "io.github.kalist28"
version = "alpha-0.0.1"

mavenPublishing {
    coordinates("io.github.kalist28", "dayschedule", "alpha-0.0.1")
    pom {
        name.set("MPDaySchedule")
        description.set("Kotlin Compose Multiplatform UI library that simplifies usage of day schedule.")
        inceptionYear.set("2024")
        url.set("https://github.com/kalist28/MPDaySchedule")
        licenses {
            license {
                name.set("GPL-3.0 License")
                url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                distribution.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
            }
        }
        developers {
            developer {
                id.set("dmitry.kalistratov")
                name.set("Dmitry Kalistratov")
                url.set("https://github.com/kalist28/")
            }
        }
        scm {
            url.set("https://github.com/kalist28/MPDaySchedule")
        }
    }
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                    freeCompilerArgs.add("-Xjdk-release=${JavaVersion.VERSION_1_8}")
                }
            }
        }
        publishLibraryVariants("release", "debug")
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
        }

        iosMain.dependencies {
        }

    }
}

android {
    namespace = "io.github.kalist28.mp.daySchedule"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
}

compose.experimental {
    web.application {}
}

tasks.register("testClasses")

