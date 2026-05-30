package io.github.loskovdm.timetracker.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import io.github.loskovdm.timetracker.database.sync.SyncDefaults

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        val guestId = SyncDefaults.LOCAL_OWNER_ID
        connection.execSQL(
            "UPDATE projects SET user_id = '$guestId' WHERE user_id = ''",
        )
        connection.execSQL(
            "UPDATE tasks SET user_id = '$guestId' WHERE user_id = ''",
        )
        connection.execSQL(
            "UPDATE time_entries SET user_id = '$guestId' WHERE user_id = ''",
        )
    }
}

/**
 * Removes local-only columns that are not present on the server schema.
 * No-op if the database never had those columns (e.g. fresh install after [MIGRATION_1_2] was simplified).
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        dropColumnIfExists(connection, "projects", "created_at")
        dropColumnIfExists(connection, "projects", "updated_at")
        dropColumnIfExists(connection, "tasks", "created_at")
        dropColumnIfExists(connection, "tasks", "updated_at")
        dropColumnIfExists(connection, "time_entries", "is_archived")
        dropColumnIfExists(connection, "time_entries", "created_at")
        dropColumnIfExists(connection, "time_entries", "updated_at")
    }
}

private fun dropColumnIfExists(connection: SQLiteConnection, table: String, column: String) {
    if (tableHasColumn(connection, table, column)) {
        connection.execSQL("ALTER TABLE $table DROP COLUMN $column")
    }
}

private fun tableHasColumn(connection: SQLiteConnection, table: String, column: String): Boolean {
    var found = false
    connection.prepare("PRAGMA table_info($table)").use { statement ->
        while (statement.step()) {
            val name = statement.getText(1)
            if (name == column) {
                found = true
                break
            }
        }
    }
    return found
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_2_3)
