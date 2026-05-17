package io.github.loskovdm.timetracker.feature.navigation.impl.scenedecorator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.contains
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.SceneMetadata
import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination

internal data class SafeAreaContentScene<T : TimeTrackerDestination>(
    private val scene: Scene<T>,
) : Scene<T> by scene {
    override val key = scene::class to scene.key

    override val metadata = scene.metadata

    override val content = @Composable {
        val deviceConfiguration = LocalDeviceConfiguration.current
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                    PaddingValues()
                } else {
                    PaddingValues(end = 16.dp)
                })
                .windowInsetsPadding(WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Top
                ))
                .clip(
                    shape = if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                        RectangleShape
                    } else {
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                        )
                    }
                )
        ) {
            scene.content()
        }
    }
}

@Composable
fun <T : TimeTrackerDestination> rememberSafeAreaContentSceneDecoratorStrategy(): SafeAreaContentSceneDecoratorStrategy<T> {
    return SafeAreaContentSceneDecoratorStrategy()
}

class SafeAreaContentSceneDecoratorStrategy<T : TimeTrackerDestination> : SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> {
        return if (!scene.metadata.contains(SceneMetadata.SceneTypeKey)) {
            scene
        } else {
            SafeAreaContentScene(scene = scene)
        }
    }
}