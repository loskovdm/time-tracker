import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    dependencies {
        implementation(projects.shared)
        implementation(projects.data.supabaseclient)
        implementation(projects.domain)
        implementation(projects.designSystem)

        implementation(compose.desktop.currentOs)
        implementation(libs.kotlinx.coroutines.swing)
        implementation(libs.androidx.lifecycle.runtimeCompose)

        implementation(libs.koin.core)
    }
}

compose.desktop {
    application {
        mainClass = "io.github.loskovdm.timetracker.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.github.loskovdm.timetracker"
            packageVersion = "1.0.0"

            windows {
                iconFile.set(project.file("icons/icon.ico"))
            }
            linux {
                iconFile.set(project.file("icons/icon.png"))
            }
        }
    }
}