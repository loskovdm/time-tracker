plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    androidLibrary {
        namespace = "io.github.loskovdm.timetracker.shared"
        compileSdk = 36
        minSdk = 24
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.timer)
            implementation(projects.feature.calendar)
            implementation(projects.feature.reports)
            implementation(projects.feature.projects)
            implementation(projects.feature.settings)
            implementation(projects.designSystem)

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
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
