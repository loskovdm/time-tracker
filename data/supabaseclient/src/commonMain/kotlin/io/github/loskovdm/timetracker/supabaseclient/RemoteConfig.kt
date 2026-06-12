package io.github.loskovdm.timetracker.supabaseclient

data class RemoteConfig(
    val supabaseUrl: String = "",
    val supabaseAnonKey: String = "",
    val powerSyncUrl: String = "",
)