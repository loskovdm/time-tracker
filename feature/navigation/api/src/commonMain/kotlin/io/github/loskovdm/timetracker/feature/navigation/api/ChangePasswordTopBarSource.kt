package io.github.loskovdm.timetracker.feature.navigation.api

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ChangePasswordTopBarMode {
    ResetPassword,
    ChangePassword,
    VerifyCode,
    NewPassword,
}

interface ChangePasswordTopBarSource {
    val mode: StateFlow<ChangePasswordTopBarMode>
    fun setMode(mode: ChangePasswordTopBarMode)
    fun setBackInterceptor(interceptor: (() -> Boolean)?)
    fun tryBack(): Boolean
}

class DefaultChangePasswordTopBarSource : ChangePasswordTopBarSource {
    private val _mode = MutableStateFlow(ChangePasswordTopBarMode.ResetPassword)
    override val mode: StateFlow<ChangePasswordTopBarMode> = _mode.asStateFlow()
    private var backInterceptor: (() -> Boolean)? = null

    override fun setMode(mode: ChangePasswordTopBarMode) {
        _mode.value = mode
    }

    override fun setBackInterceptor(interceptor: (() -> Boolean)?) {
        backInterceptor = interceptor
    }

    override fun tryBack(): Boolean = backInterceptor?.invoke() == true
}
