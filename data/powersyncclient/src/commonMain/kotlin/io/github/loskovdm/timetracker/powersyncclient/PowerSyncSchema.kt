package io.github.loskovdm.timetracker.powersyncclient

import com.powersync.db.schema.RawTable
import com.powersync.db.schema.RawTableSchema
import com.powersync.db.schema.Schema

internal fun createPowerSyncSchema(): Schema =
    Schema(
        RawTable(name = "projects", schema = RawTableSchema()),
        RawTable(name = "tasks", schema = RawTableSchema()),
        RawTable(name = "time_entries", schema = RawTableSchema()),
    )
