package io.github.loskovdm.timetracker.powersyncclient

import co.touchlab.kermit.Logger
import com.powersync.db.driver.SQLiteConnectionLease
import com.powersync.db.schema.Schema
import com.powersync.integrations.room.RoomConnectionPool

internal suspend fun installRawTableCrudTriggers(
    connectionPool: RoomConnectionPool,
    schema: Schema,
    logger: Logger,
) {
    connectionPool.write { lease ->
        installTriggersOnLease(lease, schema, logger)
    }
}

private fun installTriggersOnLease(
    lease: SQLiteConnectionLease,
    schema: Schema,
    logger: Logger,
) {
    for (rawTable in schema.rawTables) {
        val tableJson = rawTable.jsonDescription()
        for (write in CRUD_WRITE_TYPES) {
            val triggerName = "${rawTable.name}_powersync_$write"
            lease.usePreparedSync("DROP TRIGGER IF EXISTS \"$triggerName\"") { statement ->
                statement.step()
            }
            lease.usePreparedSync(
                "SELECT powersync_create_raw_table_crud_trigger(?, ?, ?)",
            ) { statement ->
                statement.bindText(1, tableJson)
                statement.bindText(2, triggerName)
                statement.bindText(3, write)
                check(statement.step())
            }
        }
        logger.d { "PowerSync CRUD triggers installed for ${rawTable.name}" }
    }
}

private val CRUD_WRITE_TYPES = listOf("INSERT", "UPDATE", "DELETE")
