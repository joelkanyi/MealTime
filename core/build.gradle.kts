plugins {
    id(Plugins.androidLibrary)
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.kanyideveloper.mealtime.core"
}

dependencies {
    // Modules
    /*implementation(project(Modules.composeUi))*/
}