package io.github.loskovdm.androidapp

import android.app.Application
import io.github.loskovdm.timetracker.di.initKoin
import org.koin.android.ext.koin.androidContext

class TimeTrackerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TimeTrackerApplication)
        }
    }
}