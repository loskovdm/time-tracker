package io.github.loskovdm.timetracker.sync



import io.github.loskovdm.domain.repository.SyncRepository

import io.github.loskovdm.timetracker.powersyncclient.PowerSyncEngine



class SyncRepositoryImpl(

    private val engine: PowerSyncEngine,

) : SyncRepository {

    override suspend fun connect() {

        engine.connect()

    }



    override suspend fun disconnect() {

        engine.disconnect()

    }



    override suspend fun prepareLocalUploadPipeline() {

        engine.ensureUploadPipelineReady()

    }



    override suspend fun flushPendingLocalUploads() {

        engine.flushPendingRoomUploads()

    }

    override suspend fun clearPendingUploads() {

        engine.clearPendingUploads()

    }

}


