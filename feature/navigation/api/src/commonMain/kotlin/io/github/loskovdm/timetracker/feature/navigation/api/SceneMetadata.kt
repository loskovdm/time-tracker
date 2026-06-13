package io.github.loskovdm.timetracker.feature.navigation.api

import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata

enum class SceneType {
    Timer,
    Calendar,
    ActiveProjects,
    ArchivedProjects,
    Tasks,
    Reports,
    Settings,
    Auth,
    ChangePassword,
}

object SceneMetadata {
    object SceneTypeKey : NavMetadataKey<SceneType>
}

object SceneMetadataBuilder {
    fun timer() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.Timer)
    }

    fun calendar() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.Calendar)
    }

    fun activeProjects() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.ActiveProjects)
    }

    fun archivedProjects() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.ArchivedProjects)
    }

    fun tasks() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.Tasks)
    }

    fun reports() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.Reports)
    }

    fun settings() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.Settings)
    }

    fun auth() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.Auth)
    }

    fun changePassword() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.ChangePassword)
    }
}