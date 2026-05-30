package io.github.loskovdm.timetracker.datastore

import org.koin.core.module.Module
import org.koin.dsl.module

val datastoreModule = module {
    includes(datastorePlatformModule)
}

expect val datastorePlatformModule: Module
