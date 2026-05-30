package io.github.loskovdm.timetracker.powersyncclient

import com.powersync.integrations.room.RoomConnectionPool

/**
 * Drops pending client uploads so local-only deletes (e.g. on sign-out) are not sent to the server.
 * See https://docs.powersync.com/client-sdks/advanced/local-only-usage
 */
internal suspend fun clearPowerSyncUploadQueue(connectionPool: RoomConnectionPool) {
    connectionPool.write { lease ->
        lease.usePreparedSync("DELETE FROM ps_crud") { statement ->
            while (statement.step()) {
                // drain result
            }
        }
    }
}
