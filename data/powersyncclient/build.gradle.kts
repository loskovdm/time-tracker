plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "io.github.loskovdm.timetracker.powersyncclient"
        compileSdk = 37
        minSdk = 24
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.data.database)
            implementation(projects.data.supabaseclient)

            implementation(libs.powersync.core)
            implementation(libs.powersync.connector.supabase)
            implementation(libs.powersync.integration.room)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation("co.touchlab:kermit:2.0.8")
        }
    }
}
