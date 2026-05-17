plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {

    android {
        namespace = "io.github.loskovdm.timetracker.feature.projects.api"
        compileSdk = 37
        minSdk = 24
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.navigation.api)

                implementation(libs.kotlin.stdlib)
                implementation(libs.androidx.lifecycle.viewmodel)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}