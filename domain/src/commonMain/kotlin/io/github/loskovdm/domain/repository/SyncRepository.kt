package io.github.loskovdm.domain.repository

interface SyncRepository {
    suspend fun connect()

    suspend fun disconnect()

    /** Installs PowerSync CRUD triggers before guest data is re-inserted for upload. */
    suspend fun prepareLocalUploadPipeline()

    /** Flushes Room writes into the PowerSync upload queue. */
    suspend fun flushPendingLocalUploads()

    /** Drops queued uploads after local-only deletes (must run after those deletes). */
    suspend fun clearPendingUploads()
}
