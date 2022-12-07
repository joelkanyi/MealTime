plugins {
    id(Plugins.androidLibrary)
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version ("1.7.10-1.0.6")
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    compileSdk = AndroidConfig.compileSDK

    defaultConfig {
        minSdk = AndroidConfig.minSDK
        targetSdk = AndroidConfig.targetSDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
}

dependencies {
    // Modules
    implementation(project(Modules.core))

    implementation("androidx.activity:activity-compose:1.6.1")

    implementation("androidx.compose.ui:ui:${Versions.composeCompiler}")
    implementation("androidx.compose.material:material:${Versions.composeCompiler}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.composeCompiler}")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.composeCompiler}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.composeCompiler}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.composeCompiler}")

    // Compose dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.navigation:navigation-compose:2.6.0-alpha04")
    implementation("com.google.accompanist:accompanist-flowlayout:0.27.0")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Paging Compose
    implementation("androidx.paging:paging-compose:1.0.0-alpha17")

    // Livedata
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.composeCompiler}")

    // collapsing Toolbar
    implementation("me.onebone:toolbar-compose:2.3.2")

    // Material 3
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.0.1")

    // Searchable Dropdown
    implementation("com.github.Breens-Mbaka:Searchable-Dropdown-Menu-Jetpack-Compose:0.2.4")

    // Swipe to refresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.27.1")

    // Pager
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")

    // Insets
    implementation("com.google.accompanist:accompanist-insets:0.17.0")

    // Accompanist System UI controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    // RamCosta Navigation
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.7.27-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.7.27-beta")

    // Navigation animation
    implementation("com.google.accompanist:accompanist-navigation-animation:0.27.0")
}
