import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.koin.compiler)
}

val generateJvmRemoteConfig = tasks.register("generateJvmRemoteConfig") {
    val outputDirectory = layout.buildDirectory.dir("generated/remoteConfig/jvmMain/resources")
    val localPropertiesFile = rootProject.file("local.properties")

    inputs.file(localPropertiesFile).optional()
    outputs.dir(outputDirectory)

    doLast {
        val localProperties = Properties()
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { localProperties.load(it) }
        }

        fun resolveProperty(name: String): String =
            localProperties.getProperty(name)
                ?: System.getenv(name)
                ?: ""

        val supabaseUrl = resolveProperty("SUPABASE_URL")
        val supabaseAnonKey = resolveProperty("SUPABASE_ANON_KEY")
        val powerSyncUrl = resolveProperty("POWERSYNC_URL")

        if (supabaseUrl.isBlank() || supabaseAnonKey.isBlank()) {
            logger.warn(
                "SUPABASE_URL and SUPABASE_ANON_KEY are missing. " +
                    "Set them in local.properties or environment variables before building the desktop MSI.",
            )
        }

        val propertiesFile = outputDirectory.get().file("remote-config.properties").asFile
        propertiesFile.parentFile.mkdirs()
        propertiesFile.bufferedWriter().use { writer ->
            writer.appendLine("SUPABASE_URL=$supabaseUrl")
            writer.appendLine("SUPABASE_ANON_KEY=$supabaseAnonKey")
            writer.appendLine("POWERSYNC_URL=$powerSyncUrl")
        }
    }
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
            implementation(projects.data.supabaseclient)
            implementation(projects.data.powersyncclient)
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
            implementation(projects.feature.auth.api)
            implementation(projects.feature.auth.impl)

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

        jvmMain {
            resources.srcDir(
                generateJvmRemoteConfig.map {
                    layout.buildDirectory.dir("generated/remoteConfig/jvmMain/resources")
                },
            )
        }
    }
}

tasks.matching { it.name == "jvmProcessResources" }.configureEach {
    dependsOn(generateJvmRemoteConfig)
}
