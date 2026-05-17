package io.github.loskovdm.timetracker.feature.navigation.api

import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.metadata

enum class SceneType {
    Timer,
    Calendar,
    Projects,
    Tasks,
    Reports,
    Settings
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

    fun projects() = metadata {
        put(SceneMetadata.SceneTypeKey, SceneType.Projects)
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
}