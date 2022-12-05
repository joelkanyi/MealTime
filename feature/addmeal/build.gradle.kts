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
    namespace = "com.kanyideveloper.mealtime.addmeal"

    ksp {
        arg("compose-destinations.moduleName", Modules.addMeal)
        arg("compose-destinations.mode", "destinations")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

dependencies {

    // RamCosta Navigation
    implementation("io.github.raamcosta.compose-destinations:animations-core:1.7.27-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.7.27-beta")

    // Navigation animation
    implementation("com.google.accompanist:accompanist-navigation-animation:0.27.0")

    // Modules
    implementation(project(Modules.core))
    implementation(project(Modules.composeUi))
}