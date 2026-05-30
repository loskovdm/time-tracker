plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "io.github.loskovdm.timetracker.feature.auth.api"
        compileSdk = 37
        minSdk = 24
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.navigation.api)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
