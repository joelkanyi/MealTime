plugins {
    id(Plugins.androidApplication)
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version ("1.7.20-1.0.8")
    id("com.google.gms.google-services")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
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
    implementation(project(Modules.core))
    implementation(project(Modules.composeUi))

    implementation(project(Modules.home))
    implementation(project(Modules.search))
    implementation(project(Modules.favorites))
    implementation(project(Modules.settings))
    implementation(project(Modules.addMeal))
    implementation(project(Modules.mealPlanner))
    implementation(project(Modules.kitchenTimer))
    implementation(project(Modules.auth))

    // RamCosta Navigation
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.7.32-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.7.32-beta")

    // Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.0")
}
