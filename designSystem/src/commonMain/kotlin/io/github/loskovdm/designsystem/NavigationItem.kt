package io.github.loskovdm.designsystem

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class NavigationItem(
    val title: StringResource,
    val iconOutlined: DrawableResource,
    val iconFilled: DrawableResource,
) {
    fun icon(selected: Boolean) = if (selected) iconFilled else iconOutlined
}