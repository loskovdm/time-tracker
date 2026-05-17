//package io.github.loskovdm.timetracker.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
//import androidx.navigation3.runtime.NavKey
//import androidx.navigation3.runtime.entryProvider
//import androidx.navigation3.runtime.rememberNavBackStack
//import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
//import androidx.navigation3.ui.NavDisplay
//import androidx.savedstate.serialization.SavedStateConfiguration
//import io.github.loskovdm.designsystem.util.DeviceConfiguration
//import io.github.loskovdm.settings.SettingsNavigation
//import kotlinx.serialization.modules.SerializersModule
//import kotlinx.serialization.modules.polymorphic
//
//@Composable
//fun RootNavigation(
//    modifier: Modifier = Modifier,
//    deviceConfiguration: DeviceConfiguration,
//) {
//    val rootBackStack = rememberNavBackStack(
//        configuration = SavedStateConfiguration {
//            serializersModule = SerializersModule {
//                polymorphic(NavKey::class) {
//                    subclass(Route.Home::class, Route.Home.serializer())
//                    subclass(Route.Settings::class, Route.Settings.serializer())
//                }
//            }
//        },
//        Route.Home
//    )
//    NavDisplay(
//        modifier = modifier,
//        backStack = rootBackStack,
//        entryDecorators = listOf(
//            rememberSaveableStateHolderNavEntryDecorator(),
//            rememberViewModelStoreNavEntryDecorator()
//        ),
//        entryProvider = entryProvider {
//            entry<Route.Home> {
//                HomeNavigation(
//                    deviceConfiguration = deviceConfiguration,
//                    navigationItems = TOP_LEVEL_DESTINATIONS,
//                    onSettings = {
//                        rootBackStack.add(Route.Settings)
//                    },
//                )
//            }
//            entry<Route.Settings> {
//                SettingsNavigation(
//                    deviceConfiguration = deviceConfiguration,
//                    onBack = {
//                        rootBackStack.remove(Route.Settings)
//                    }
//                )
//            }
//        }
//    )
//}