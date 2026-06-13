package io.github.loskovdm.timetracker.feature.navigation.api

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthTopBarMode {
    SignIn,
    SignUp,
    VerifyEmail,
}

interface AuthTopBarModeSource {
    val mode: StateFlow<AuthTopBarMode>
    fun setMode(mode: AuthTopBarMode)
    fun setBackInterceptor(interceptor: (() -> Boolean)?)
    fun tryBack(): Boolean
}

class DefaultAuthTopBarModeSource : AuthTopBarModeSource {
    private val _mode = MutableStateFlow(AuthTopBarMode.SignIn)
    override val mode: StateFlow<AuthTopBarMode> = _mode.asStateFlow()
    private var backInterceptor: (() -> Boolean)? = null

    override fun setMode(mode: AuthTopBarMode) {
        _mode.value = mode
    }

    override fun setBackInterceptor(interceptor: (() -> Boolean)?) {
        backInterceptor = interceptor
    }

    override fun tryBack(): Boolean = backInterceptor?.invoke() == true
}
