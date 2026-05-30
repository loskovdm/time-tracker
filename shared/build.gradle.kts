plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.koin.compiler)
}

kotlin {

    android {
        namespace = "io.github.loskovdm.timetracker.shared"
        compileSdk = 37
        minSdk = 24
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        withHostTest {}
    }
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.designSystem)
            implementation(projects.domain)
            implementation(projects.data.datastore)
            implementation(projects.data.database)
            implementation(projects.feature.navigation.impl)
            implementation(projects.feature.navigation.api)
            implementation(projects.feature.timeentry.api)
            implementation(projects.feature.timeentry.impl)
            implementation(projects.feature.projects.api)
            implementation(projects.feature.projects.impl)
            implementation(projects.feature.tasks.api)
            implementation(projects.feature.tasks.impl)
            implementation(projects.feature.reports.api)
            implementation(projects.feature.reports.impl)
            implementation(projects.feature.settings.api)
            implementation(projects.feature.settings.impl)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3Adaptive)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.nav3)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.navigation3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
