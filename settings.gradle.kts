pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "MealTime"
include(":app")
include(":feature:addmeal")
include(":core")
include(":feature:settings")
include(":compose-ui")
include(":core-database")
include(":core-network")
include(":feature:mealplanner")
include(":feature:kitchen-timer")
include(":feature:auth")
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":feature:home:di")
include(":feature:search:data")
include(":feature:search:domain")
include(":feature:search:presentation")
include(":feature:search:di")
include(":feature:favorites:domain")
include(":feature:favorites:data")
include(":feature:favorites:presentation")
include(":feature:favorites:di")
