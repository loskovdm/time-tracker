package io.github.loskovdm.designsystem.component

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.navigation.NavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
) {
    BottomAppBar(
        modifier = modifier
    ) {
        navigationItems.forEach { (item, data) ->
            val selected = item == selectedNavigationItem
            NavigationBarItem(
                selected = selected,
                onClick = {
                    onSelectedNavigationItem(item)
                },
                icon = {
                    Icon(
                        painter = painterResource(data.icon(selected)),
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