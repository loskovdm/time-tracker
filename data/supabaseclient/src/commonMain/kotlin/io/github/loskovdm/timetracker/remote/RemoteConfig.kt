package io.github.loskovdm.timetracker.remote

data class RemoteConfig(
    val supabaseUrl: String = "",
    val supabaseAnonKey: String = "",
    val powerSyncUrl: String = "",
)
