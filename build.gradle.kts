@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.jvm) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.parcelize) apply false
    alias(libs.plugins.ksp) apply false
}

subprojects {

    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint().userData(mapOf("disabled_rules" to "filename"))
            licenseHeaderFile(
                rootProject.file("${project.rootDir}/spotless/copyright.kt"),
                "^(package|object|import|interface)"
            )
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("misc") {
            target("**/*.md", "**/.gitignore")
            trimTrailingWhitespace()
            indentWithTabs()
            endWithNewline()
        }
        java {
            target("src/*/java/**/*.java")
            googleJavaFormat("1.7").aosp()
            indentWithSpaces()
            licenseHeaderFile(rootProject.file("spotless/copyright.java"))
            removeUnusedImports()
        }
        groovyGradle {
            target("**/*.gradle")
        }
    }

    repositories {
        mavenCentral()
    }
}
