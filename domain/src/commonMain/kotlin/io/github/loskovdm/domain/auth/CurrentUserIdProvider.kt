package io.github.loskovdm.domain.auth

interface CurrentUserIdProvider {
    fun getUserIdForNewRecords(): String
}
