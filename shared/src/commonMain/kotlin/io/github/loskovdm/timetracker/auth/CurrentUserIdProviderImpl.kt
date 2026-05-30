package io.github.loskovdm.timetracker.auth

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.domain.auth.GuestUserIds
import io.github.loskovdm.domain.repository.AuthRepository

class CurrentUserIdProviderImpl(
    private val authRepository: AuthRepository,
) : CurrentUserIdProvider {
    override fun getUserIdForNewRecords(): String =
        authRepository.currentUserId() ?: GuestUserIds.LOCAL
}
