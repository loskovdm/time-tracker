package io.github.loskovdm.timetracker.powersyncclient

import io.github.loskovdm.timetracker.database.TimeTrackerDatabase

interface PowerSyncEngine {
    val roomDatabase: TimeTrackerDatabase

    suspend fun connect()

    suspend fun disconnect()

    suspend fun ensureUploadPipelineReady()

    suspend fun flushPendingRoomUploads()

    suspend fun clearPendingUploads()
}
