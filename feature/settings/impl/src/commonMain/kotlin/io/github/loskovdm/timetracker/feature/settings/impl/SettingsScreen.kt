package io.github.loskovdm.timetracker.feature.settings.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.domain.model.AppLanguage
import io.github.loskovdm.domain.model.ThemeMode
import io.github.loskovdm.timetracker.feature.settings.impl.presentation.SettingsViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.settings_account
import timetracker.designsystem.generated.resources.settings_account_coming_soon
import timetracker.designsystem.generated.resources.settings_account_description
import timetracker.designsystem.generated.resources.settings_appearance
import timetracker.designsystem.generated.resources.settings_language
import timetracker.designsystem.generated.resources.settings_language_english
import timetracker.designsystem.generated.resources.settings_language_russian
import timetracker.designsystem.generated.resources.settings_open_auth
import timetracker.designsystem.generated.resources.settings_theme_dark
import timetracker.designsystem.generated.resources.settings_theme_light
import timetracker.designsystem.generated.resources.settings_theme_system

@Composable
internal fun SettingsScreen(
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SettingsAccountStubSection()

        SettingsSectionCard(title = stringResource(Res.string.settings_appearance)) {
            SettingsRadioGroup(
                selected = themeMode,
                options = ThemeMode.entries,
                label = ::themeModeLabel,
                onSelected = viewModel::onThemeModeSelected,
            )
        }

        SettingsSectionCard(title = stringResource(Res.string.settings_language)) {
            SettingsRadioGroup(
                selected = appLanguage,
                options = listOf(AppLanguage.ENGLISH, AppLanguage.RUSSIAN),
                label = ::appLanguageLabel,
                onSelected = viewModel::onAppLanguageSelected,
            )
        }
    }
}

@Composable
private fun SettingsAccountStubSection() {
    SettingsSectionCard(title = stringResource(Res.string.settings_account)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(Res.string.settings_account_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(Res.string.settings_account_coming_soon),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(Res.string.settings_open_auth))
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            content()
        }
    }
}

@Composable
private fun <T> SettingsRadioGroup(
    selected: T,
    options: List<T>,
    label: @Composable (T) -> String,
    onSelected: (T) -> Unit,
) {
    Column(modifier = Modifier.selectableGroup()) {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected == option,
                        onClick = { onSelected(option) },
                        role = Role.RadioButton,
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                RadioButton(
                    selected = selected == option,
                    onClick = null,
                )
                Text(
                    text = label(option),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun themeModeLabel(mode: ThemeMode): String {
    return when (mode) {
        ThemeMode.SYSTEM -> stringResource(Res.string.settings_theme_system)
        ThemeMode.LIGHT -> stringResource(Res.string.settings_theme_light)
        ThemeMode.DARK -> stringResource(Res.string.settings_theme_dark)
    }
}

@Composable
private fun appLanguageLabel(language: AppLanguage): String {
    return when (language) {
        AppLanguage.ENGLISH -> stringResource(Res.string.settings_language_english)
        AppLanguage.RUSSIAN -> stringResource(Res.string.settings_language_russian)
    }
}
