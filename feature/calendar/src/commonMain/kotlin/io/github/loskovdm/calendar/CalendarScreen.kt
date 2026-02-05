package io.github.loskovdm.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    appBottomBar: @Composable () -> Unit,
    titleResource: StringResource,
    settingsIconResource: DrawableResource,
    onSettings: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(titleResource))
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSettings()
                        }
                    ) {
                        Icon(
                            painter = painterResource(settingsIconResource),
                            contentDescription = stringResource(titleResource)
                        )
                    }
                }
            )
        },
        bottomBar = {
            appBottomBar()
        },
    ) { innerPadding ->
        Box(
            modifier = modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(titleResource))
            }
        }
    }
}