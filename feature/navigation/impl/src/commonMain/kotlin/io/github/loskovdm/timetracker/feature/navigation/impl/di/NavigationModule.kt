package io.github.loskovdm.timetracker.feature.navigation.impl.di

import androidx.navigation3.scene.DialogSceneStrategy
import io.github.loskovdm.designsystem.component.ErrorDialog
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.navigation.api.TimerErrorDialogDestination
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {
    navigation<TimerErrorDialogDestination>(
        metadata = DialogSceneStrategy.dialog()
    ) { key ->
        ErrorDialog(
            message = key.errorMessage,
            onDismiss = {
                key.clearError()
                get<Navigator>().goBack()
            },
        )
    }
}