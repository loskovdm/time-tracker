package io.github.loskovdm.timetracker.feature.projects.impl.presentation.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.component.EditorHeader
import io.github.loskovdm.designsystem.component.TimeTrackerTextField
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.project

@Composable
internal fun ProjectEditor(
    onClose: () -> Unit,
    editorViewModel: ProjectEditorViewModel,
) {
    val state by editorViewModel.state.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    if (state.isNew) {
        LaunchedEffect(Unit) {
            delay(300)
            focusRequester.requestFocus()
        }
    }

    val colorList = ProjectColor.entries.toList()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditorHeader(
            onClose = onClose,
            onSave = {
                val result = editorViewModel.onSaveProject()
                if (result) {
                    onClose()
                }
            },
            isAvailableSave = state.validationError == null,
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
                label = stringResource(Res.string.project),
                text = state.name,
                onTextChange = editorViewModel::onNameChanged,
                error = state.validationError,
            )
            ColorSelector(
                colorList = colorList,
                selectedColor = state.color,
                onColorChange = editorViewModel::onColorChanged,
            )
        }
    }
}

@Composable
private fun ColorSelector(
    modifier: Modifier = Modifier,
    colorList: List<ProjectColor>,
    selectedColor: ProjectColor,
    onColorChange: (ProjectColor) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        items(
            items = (colorList),
            itemContent = { color ->
                val isSelected = color == selectedColor
                ColorItem(
                    projectColor = color,
                    isSelected = isSelected,
                    onClick = {
                        onColorChange(color)
                    }
                )
            }
        )
    }
}

@Composable
private fun ColorItem(
    modifier: Modifier = Modifier,
    projectColor: ProjectColor,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color = projectColor.color)
                .selectable(
                    role = Role.RadioButton,
                    selected = isSelected,
                    onClick = onClick,
                ),
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
            )
        }
    }
}