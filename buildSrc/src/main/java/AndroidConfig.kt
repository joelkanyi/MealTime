import org.gradle.api.JavaVersion

/**
 * A collection of configuration properties for Android modules.
 */
object AndroidConfig {
    const val minSDK = 21
    const val targetSDK = 33
    const val compileSDK = 33
    const val versionCode = 3
    const val versionName = "1.0.1"
    const val applicationId = "com.kanyideveloper.mealtime"

    val javaVersion = JavaVersion.VERSION_11
}