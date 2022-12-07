plugins {
    id(Plugins.androidLibrary)
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version ("1.7.10-1.0.6")
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    compileSdk = AndroidConfig.compileSDK

    kotlinOptions {
        jvmTarget = AndroidConfig.javaVersion.toString()
    }

    namespace = "com.s_h_y_a.composeMultiModuleSample.testmodule"
}

dependencies {
    // RamCosta Navigation
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.7.27-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.7.27-beta")
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "testmodule")
}