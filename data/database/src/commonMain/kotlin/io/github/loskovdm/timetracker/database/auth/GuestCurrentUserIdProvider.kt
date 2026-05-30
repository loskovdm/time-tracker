package io.github.loskovdm.timetracker.database.auth

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.domain.auth.GuestUserIds

/**
 * Default until [io.github.loskovdm.timetracker.auth.CurrentUserIdProviderImpl] is registered in Koin.
 */
internal class GuestCurrentUserIdProvider : CurrentUserIdProvider {
    override fun getUserIdForNewRecords(): String = GuestUserIds.LOCAL
}
