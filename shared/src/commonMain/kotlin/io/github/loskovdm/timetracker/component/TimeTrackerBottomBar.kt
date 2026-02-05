package io.github.loskovdm.timetracker.component

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.timetracker.navigation.TOP_LEVEL_DESTINATIONS
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimeTrackerBottomBar(
    selectedItem: NavKey,
    onSelectedItem: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { (topLevelDestination, data) ->
            NavigationBarItem(
                selected = topLevelDestination == selectedItem,
                onClick = {
                    onSelectedItem(topLevelDestination)
                },
                icon = {
                    Icon(
                        painter = painterResource(data.iconResource),
                        contentDescription = data.title
                    )
                },
                label = {
                    Text(data.title)
                }
            )
        }
    }
}