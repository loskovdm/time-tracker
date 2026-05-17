rootProject.name = "TimeTracker"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":shared")
include(":androidApp")
include(":desktopApp")
include(":server")
include(":domain")
//include(":feature:timer_deprecated")
//include(":feature:calendar")
//include(":feature:reports")
//include(":feature:projects")
//include(":feature:settings")
include(":designSystem")
include(":data:database")
include(":data:repository")
include(":feature:timeentry:api")
include(":feature:timeentry:impl")
include(":feature:navigation:api")
include(":feature:navigation:impl")
include(":feature:projects:api")
include(":feature:projects:impl")
include(":feature:tasks:api")
include(":feature:tasks:impl")
include(":feature:reports:api")
include(":feature:reports:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
