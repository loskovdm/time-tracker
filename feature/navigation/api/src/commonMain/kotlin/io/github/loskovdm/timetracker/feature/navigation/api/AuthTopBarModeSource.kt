package io.github.loskovdm.timetracker.feature.navigation.api

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthTopBarMode {
    SignIn,
    SignUp,
}

interface AuthTopBarModeSource {
    val mode: StateFlow<AuthTopBarMode>
    fun setMode(mode: AuthTopBarMode)
}

class DefaultAuthTopBarModeSource : AuthTopBarModeSource {
    private val _mode = MutableStateFlow(AuthTopBarMode.SignIn)
    override val mode: StateFlow<AuthTopBarMode> = _mode.asStateFlow()

    override fun setMode(mode: AuthTopBarMode) {
        _mode.value = mode
    }
}
