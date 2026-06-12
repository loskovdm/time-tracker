package io.github.loskovdm.timetracker.powersyncclient

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import co.touchlab.kermit.Logger
import com.powersync.PowerSyncDatabase
import com.powersync.connector.supabase.SupabaseConnector
import com.powersync.integrations.room.RoomConnectionPool
import com.powersync.integrations.room.loadPowerSyncExtension
import io.github.jan.supabase.SupabaseClient
import io.github.loskovdm.timetracker.database.TimeTrackerDatabase
import io.github.loskovdm.timetracker.database.getDatabaseBuilder
import io.github.loskovdm.timetracker.supabaseclient.RemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class PowerSyncEngineImpl(
    private val remoteConfig: RemoteConfig,
    private val supabaseClient: SupabaseClient,
) : PowerSyncEngine {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val schema = createPowerSyncSchema()
    private val logger = Logger.withTag("PowerSync")
    private val setupMutex = Mutex()
    private var crudTriggersInstalled = false

    override val roomDatabase: TimeTrackerDatabase by lazy {
        val driver = BundledSQLiteDriver().also { it.loadPowerSyncExtension() }
        getDatabaseBuilder()
            .setDriver(driver)
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    private val connectionPool: RoomConnectionPool by lazy {
        RoomConnectionPool(roomDatabase, schema)
    }

    private val powerSyncDatabase: PowerSyncDatabase by lazy {
        PowerSyncDatabase.opened(
            pool = connectionPool,
            scope = scope,
            schema = schema,
            identifier = "timetracker",
            logger = logger,
        )
    }

    private val connector: SupabaseConnector by lazy {
        SupabaseConnector(
            supabaseClient = supabaseClient,
            powerSyncEndpoint = remoteConfig.powerSyncUrl,
        )
    }

    override suspend fun ensureUploadPipelineReady() {
        ensurePowerSyncReady()
    }

    override suspend fun flushPendingRoomUploads() {
        connectionPool.transferPendingRoomUpdatesToPowerSync()
    }

    override suspend fun clearPendingUploads() {
        ensurePowerSyncReady()
        clearPowerSyncUploadQueue(connectionPool)
        logger.d { "PowerSync upload queue cleared" }
    }

    override suspend fun connect() {
        ensurePowerSyncReady()
        powerSyncDatabase.connect(connector)
        connectionPool.transferPendingRoomUpdatesToPowerSync()
        logger.i { "PowerSync connected" }
    }

    override suspend fun disconnect() {
        powerSyncDatabase.disconnect()
        logger.i { "PowerSync disconnected" }
    }

    private suspend fun ensurePowerSyncReady() {
        setupMutex.withLock {
            powerSyncDatabase
            if (!crudTriggersInstalled) {
                installRawTableCrudTriggers(connectionPool, schema, logger)
                crudTriggersInstalled = true
            }
        }
    }
}