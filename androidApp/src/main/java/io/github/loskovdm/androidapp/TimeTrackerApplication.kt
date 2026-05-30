package io.github.loskovdm.androidapp

import android.app.Application

import io.github.loskovdm.designsystem.locale.applyPlatformLocale

import io.github.loskovdm.domain.repository.UserSettingsRepository

import io.github.loskovdm.timetracker.di.initKoin

import io.github.loskovdm.timetracker.supabase.RemoteConfig

import kotlinx.coroutines.flow.first

import kotlinx.coroutines.runBlocking

import org.koin.android.ext.koin.androidContext

import org.koin.core.context.GlobalContext

class TimeTrackerApplication : Application() {

    override fun onCreate() {

        super.onCreate()

        initKoin(

            remoteConfig = RemoteConfig(

                supabaseUrl = BuildConfig.SUPABASE_URL,

                supabaseAnonKey = BuildConfig.SUPABASE_ANON_KEY,

                powerSyncUrl = BuildConfig.POWERSYNC_URL,

            ),

        ) {

            androidContext(this@TimeTrackerApplication)

        }

        runBlocking {

            val localeTag = GlobalContext.get()

                .get<UserSettingsRepository>()

                .appLanguage

                .first()

                .tag

            applyPlatformLocale(localeTag)

        }

    }

}
