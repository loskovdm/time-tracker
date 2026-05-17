package io.github.loskovdm.timetracker.feature.navigation.api

import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata

object EntryMetadata {
    object TimeEntriesListKey : NavMetadataKey<Boolean>
    object TimeEntryCalendarKey : NavMetadataKey<Boolean>
    object EditorKey: NavMetadataKey<Boolean>
    object ProjectsListKey : NavMetadataKey<Boolean>
    object TasksListKey : NavMetadataKey<Boolean>
    object ReportsKey : NavMetadataKey<Boolean>
    object SettingsKey : NavMetadataKey<Boolean>
}

object EntryMetadataBuilder {
    fun timeEntriesList() = metadata {
        put(EntryMetadata.TimeEntriesListKey, true)
    }
    fun timeEntryCalendar() = metadata {
        put(EntryMetadata.TimeEntryCalendarKey, true)
    }
    fun editor() = metadata {
        put(EntryMetadata.EditorKey, true)
    }
    fun projectsList() = metadata {
        put(EntryMetadata.ProjectsListKey, true)
    }
    fun tasksList() = metadata {
        put(EntryMetadata.TasksListKey, true)
    }
    fun reports() = metadata {
        put(EntryMetadata.ReportsKey, true)
    }
    fun settings() = metadata {
        put(EntryMetadata.SettingsKey, true)
    }
}