package io.github.loskovdm.timetracker.config

import io.github.loskovdm.timetracker.supabaseclient.RemoteConfig
import java.io.File
import java.util.Properties

fun loadRemoteConfig(projectRoot: File = findProjectRoot()): RemoteConfig {
    val properties = Properties()
    val localPropertiesFile = File(projectRoot, "local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { properties.load(it) }
    }

    fun optionalProperty(name: String): String =
        properties.getProperty(name)
            ?: System.getenv(name)
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
