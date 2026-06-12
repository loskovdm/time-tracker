package io.github.loskovdm.timetracker.supabaseclient.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.loskovdm.domain.repository.AuthRepository
import io.github.loskovdm.timetracker.supabaseclient.RemoteConfig
import io.github.loskovdm.timetracker.supabaseclient.AuthRepositoryImpl
import org.koin.dsl.module

val supabaseClientModule = module {
    single<SupabaseClient> {
        val config: RemoteConfig = get()
        createSupabaseClient(
            supabaseUrl = config.supabaseUrl,
            supabaseKey = config.supabaseAnonKey,
        ) {
            install(Auth)
            install(Postgrest)
        }
    }

    single<AuthRepository> {
        AuthRepositoryImpl(client = get())
    }
}
