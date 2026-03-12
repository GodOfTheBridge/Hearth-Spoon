pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Hearth-Spoon"

include(
    ":app",
    ":core:common",
    ":core:model",
    ":core:ui",
    ":core:designsystem",
    ":core:network",
    ":core:database",
    ":domain:api",
    ":feature:home",
    ":feature:home-details",
    ":feature:profile",
    ":data:home",
    ":data:settings",
)
