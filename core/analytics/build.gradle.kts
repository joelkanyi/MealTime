import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.parcelize)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.joelkanyi.analytics"
    compileSdk = AndroidConfig.compileSDK

    defaultConfig {
        minSdk = AndroidConfig.minSDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            val mixPanelToken: String = gradleLocalProperties(rootDir).getProperty("MIXPANEL_TOKEN") ?: ""
            buildConfigField("String", "MIXPANEL_TOKEN", "\"$mixPanelToken\"")
        }

        getByName("release") {
            isMinifyEnabled = true

            val mixPanelToken: String = gradleLocalProperties(rootDir).getProperty("MIXPANEL_TOKEN") ?: ""
            buildConfigField("String", "MIXPANEL_TOKEN", "\"$mixPanelToken\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Mixpanel
    implementation(libs.mixpanel)
}
