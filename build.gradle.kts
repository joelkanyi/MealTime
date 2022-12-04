
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
} // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version ("7.3.0") apply false
    id("com.android.library") version ("7.3.0") apply false
    id("org.jetbrains.kotlin.android") version ("1.7.10") apply false
    id("org.jetbrains.kotlin.jvm") version Versions.kotlin apply false
    id("org.jlleitschuh.gradle.ktlint") version ("11.0.0")
    id("com.diffplug.spotless") version ("5.17.1")
}

/*tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}*/

subprojects {

    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(
                rootProject.file("${project.rootDir}/spotless/copyright.kt"),
                "^(package|object|import|interface)"
            )
        }
    }

    repositories {
        mavenCentral()
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        android.set(true)
        verbose.set(true)
        debug.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
    }
}
