plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version ("1.7.10-1.0.6")
}

android {
    compileSdk = AndroidConfig.compileSDK

    defaultConfig {
        applicationId = "com.kanyideveloper.mealtime"
        minSdk = AndroidConfig.minSDK
        targetSdk = AndroidConfig.targetSDK
        versionCode = 1
        versionName = "1.0.0"

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
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.compose.ui:ui:${Versions.composeCompiler}")
    implementation("androidx.compose.material:material:${Versions.composeCompiler}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.composeCompiler}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.composeCompiler}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.composeCompiler}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.composeCompiler}")

    // Room
    val room_version = "2.4.2"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.4.3")

    // Compose dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.navigation:navigation-compose:2.6.0-alpha04")
    implementation("com.google.accompanist:accompanist-flowlayout:0.27.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // RamCosta Navigation
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.7.27-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.7.27-beta")

    // Navigation animation
    implementation("com.google.accompanist:accompanist-navigation-animation:0.27.0")

    // Accompanist System UI controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Insets
    implementation("com.google.accompanist:accompanist-insets:0.17.0")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Paging Compose
    implementation("androidx.paging:paging-compose:1.0.0-alpha17")

    // Swipe to refresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.27.1")

    // Livedata
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.composeCompiler}")

    // collapsing Toolbar
    implementation("me.onebone:toolbar-compose:2.3.2")
}
