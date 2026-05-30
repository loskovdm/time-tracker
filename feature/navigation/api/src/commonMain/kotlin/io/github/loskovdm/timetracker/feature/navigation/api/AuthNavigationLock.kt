package io.github.loskovdm.timetracker.feature.navigation.api

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Blocks settings/auth back navigation while sign-in, sign-out, or guest migration runs.
 */
interface AuthNavigationLock {
    val isBlockingBack: StateFlow<Boolean>

    fun begin()

    fun end()
}

class DefaultAuthNavigationLock : AuthNavigationLock {
    private val _isBlockingBack = MutableStateFlow(false)
    override val isBlockingBack: StateFlow<Boolean> = _isBlockingBack.asStateFlow()

    private var depth = 0

    @Synchronized
    override fun begin() {
        depth++
        if (depth == 1) {
            _isBlockingBack.value = true
        }
    }

    @Synchronized
    override fun end() {
        if (depth > 0) {
            depth--
        }
        if (depth == 0) {
            _isBlockingBack.value = false
        }
    }
}
