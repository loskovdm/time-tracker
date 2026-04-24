package io.github.loskovdm.timer.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.timer.TimerViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.includes
import org.koin.plugin.module.dsl.viewModel

val timerModule = module {
    viewModel<TimerViewModel>()
    includes(domainModule)
}