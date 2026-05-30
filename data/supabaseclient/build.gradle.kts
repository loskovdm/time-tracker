plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "io.github.loskovdm.timetracker.supabaseclient"
        compileSdk = 37
        minSdk = 24
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)

            implementation(libs.supabase.auth)
            implementation(libs.supabase.postgrest)
            implementation(libs.ktor.client.cio)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
    }
}
