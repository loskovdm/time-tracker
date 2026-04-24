package io.github.loskovdm.projects.di

import io.github.loskovdm.domain.di.domainModule
import io.github.loskovdm.projects.projects.ProjectsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val projectsModule = module {
    viewModel<ProjectsViewModel>()
    includes(domainModule)
}