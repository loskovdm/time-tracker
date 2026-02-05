package io.github.loskovdm.timetracker.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import timetracker.shared.generated.resources.Res
import timetracker.shared.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTrackerTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onSettings: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(title)
        },
        actions = {
            IconButton(
                onClick = { onSettings() },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.settings),
                    contentDescription = "Settings",
                )
            }
        },
    )
}