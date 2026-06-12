package io.github.loskovdm.timetracker.powersyncclient

import com.powersync.integrations.room.RoomConnectionPool

internal suspend fun clearPowerSyncUploadQueue(connectionPool: RoomConnectionPool) {
    connectionPool.write { lease ->
        lease.usePreparedSync("DELETE FROM ps_crud") { statement ->
            while (statement.step()) {
                // drain result
            }
        }
    }
}
