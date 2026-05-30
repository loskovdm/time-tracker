package io.github.loskovdm.timetracker.feature.navigation.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

class Navigator(
    val startDestination: TimeTrackerDestination,
    topLevelDestinations: Set<TimeTrackerDestination>
) {
    private val _currentTopLevelDestination: MutableState<TimeTrackerDestination> = mutableStateOf(startDestination)
    internal val backStack = topLevelDestinations.associateWith { route ->
        mutableStateListOf(route)
    }
    val currentTopLevelDestination: TimeTrackerDestination
        get() = _currentTopLevelDestination.value

    internal val stacksInUse: List<TimeTrackerDestination>
        get() = if (_currentTopLevelDestination.value == startDestination) {
            listOf(startDestination)
        } else {
            listOf(startDestination, _currentTopLevelDestination.value)
        }

    fun goTo(destination: TimeTrackerDestination) {
        if (destination in backStack.keys) {
            _currentTopLevelDestination.value = destination
        } else {
            backStack[_currentTopLevelDestination.value]?.add(destination)
        }
    }

    fun goToReplacingCurrentIfSameType(destination: TimeTrackerDestination) {
        if (destination in backStack.keys) {
            _currentTopLevelDestination.value = destination
            return
        }

        val currentStack = backStack[_currentTopLevelDestination.value]
            ?: error("Back stack for ${_currentTopLevelDestination.value} doesn't exist")
        val currentDestination = currentStack.lastOrNull()

        if (currentDestination != null && currentDestination::class == destination::class) {
            currentStack[currentStack.lastIndex] = destination
        } else {
            currentStack.add(destination)
        }
    }

    fun goBack() {
        val currentStack = backStack[_currentTopLevelDestination.value]
            ?: error("Back stack for ${_currentTopLevelDestination.value} doesn't exist")
        val currentDestination = currentStack.last()

        if (currentDestination == _currentTopLevelDestination.value) {
            _currentTopLevelDestination.value = startDestination
        } else {
            currentStack.removeLastOrNull()
        }
    }
}

@Composable
fun Navigator.toEntries(
    entryProvider: (TimeTrackerDestination) -> NavEntry<TimeTrackerDestination>
): SnapshotStateList<NavEntry<TimeTrackerDestination>> {
    val decoratedEntries = backStack.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<TimeTrackerDestination>(),
            rememberViewModelStoreNavEntryDecorator(),
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}