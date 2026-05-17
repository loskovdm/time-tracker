package io.github.loskovdm.timetracker.feature.navigation.impl.component.navbar

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import io.github.loskovdm.timetracker.feature.navigation.impl.util.NavigationItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun NavigationBar(
    destinations: Map<TimeTrackerDestination, NavigationItem>,
    selectedDestination: TimeTrackerDestination,
    onSelectedDestination: (TimeTrackerDestination)  -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(modifier = modifier) {
        destinations.forEach { (destination, data) ->
            NavigationBarItem(
                selected = destination == selectedDestination,
                onClick = { onSelectedDestination(destination) },
                icon = {
                    Icon(
                        imageVector = vectorResource(data.icon(destination == selectedDestination)),
                        contentDescription = stringResource(data.title)
                    )
                },
                label = {
                    Text(stringResource(data.title))
                }
            )
        }
    }
}