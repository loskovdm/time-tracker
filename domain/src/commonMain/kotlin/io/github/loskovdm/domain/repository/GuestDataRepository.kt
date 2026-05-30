package io.github.loskovdm.domain.repository

interface GuestDataRepository {
    suspend fun hasGuestData(): Boolean

    suspend fun migrateToUser(userId: String)

    suspend fun clearAll()
}
