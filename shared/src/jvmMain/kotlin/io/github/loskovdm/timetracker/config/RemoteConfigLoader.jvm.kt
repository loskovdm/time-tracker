package io.github.loskovdm.timetracker.config

import io.github.loskovdm.timetracker.supabaseclient.RemoteConfig
import java.io.File
import java.util.Properties

private const val BUNDLED_CONFIG = "remote-config.properties"

private object BundledRemoteConfig {
    fun loadInto(properties: Properties) {
        javaClass.classLoader.getResourceAsStream(BUNDLED_CONFIG)?.use { stream ->
            properties.load(stream)
        }
    }
}

fun loadRemoteConfig(projectRoot: File = findProjectRoot()): RemoteConfig {
    val properties = Properties()

    BundledRemoteConfig.loadInto(properties)

    val localPropertiesFile = File(projectRoot, "local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { properties.load(it) }
    }

    fun optionalProperty(name: String): String =
        System.getenv(name)?.takeIf { it.isNotBlank() }
            ?: properties.getProperty(name)?.takeIf { it.isNotBlank() }
            ?: ""

    return RemoteConfig(
        supabaseUrl = optionalProperty("SUPABASE_URL"),
        supabaseAnonKey = optionalProperty("SUPABASE_ANON_KEY"),
        powerSyncUrl = optionalProperty("POWERSYNC_URL"),
    )
}

private fun findProjectRoot(): File {
    var current = File(System.getProperty("user.dir"))
    repeat(6) {
        if (File(current, "local.properties").exists() || File(current, "settings.gradle.kts").exists()) {
            return current
        }
        val parent = current.parentFile
        current = parent ?: return File(System.getProperty("user.dir"))
    }
    return File(System.getProperty("user.dir"))
}
