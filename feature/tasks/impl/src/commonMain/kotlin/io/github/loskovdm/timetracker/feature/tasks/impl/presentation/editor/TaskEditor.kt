package io.github.loskovdm.timetracker.feature.tasks.impl.presentation.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.component.EditorHeader
import io.github.loskovdm.designsystem.component.TimeTrackerTextField
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.task

@Composable
internal fun TaskEditor(
    onClose: () -> Unit,
    viewModel: TaskEditorViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    if (state.isNew) {
        LaunchedEffect(Unit) {
            delay(300)
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditorHeader(
            onClose = onClose,
            onSave = {
                val result = viewModel.saveTask()
                if (result) {
                    onClose()
                }
            },
            isAvailableSave = true,
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TimeTrackerTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                label = stringResource(Res.string.task),
                text = state.name,
                onTextChange = viewModel::changeName,
                error = state.validationError,
            )
        }
    }
}