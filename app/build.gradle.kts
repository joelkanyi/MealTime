@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    compileSdk = AndroidConfig.compileSDK

    defaultConfig {
        applicationId = AndroidConfig.applicationId
        minSdk = AndroidConfig.minSDK
        targetSdk = AndroidConfig.targetSDK
        versionCode = AndroidConfig.versionCode
        versionName = AndroidConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = AndroidConfig.javaVersion
        targetCompatibility = AndroidConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = AndroidConfig.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.kanyideveloper.mealtime"

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    // Modules
    implementation(projects.core.common)
    implementation(projects.core.settings)
    implementation(projects.core.designsystem)
    implementation(projects.feature.home.presentation)
    implementation(projects.feature.home.di)
    implementation(projects.feature.search.presentation)
    implementation(projects.feature.search.di)
    implementation(projects.feature.favorites.presentation)
    implementation(projects.feature.favorites.di)
    implementation(projects.feature.settings)
    implementation(projects.feature.addmeal)
    implementation(projects.feature.kitchenTimer)
    implementation(projects.feature.auth.presentation)
    implementation(projects.feature.auth.di)

    // RamCosta Navigation
    implementation(libs.compose.destinations.animations)
    ksp(libs.compose.destinations.ksp)

    implementation(libs.android.material)

    // Splash Screen API
    implementation(libs.core.splash.screen)
}
