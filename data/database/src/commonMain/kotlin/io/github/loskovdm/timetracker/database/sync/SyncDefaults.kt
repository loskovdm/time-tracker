package io.github.loskovdm.timetracker.database.sync

import kotlin.time.Clock

/**
 * Placeholder owner until Supabase Auth is wired on the client.
 * Replace with the signed-in user's id when creating local rows.
 */
import io.github.loskovdm.domain.auth.GuestUserIds

internal object SyncDefaults {
    const val LOCAL_OWNER_ID: String = GuestUserIds.LOCAL

    fun nowIso(): String = Clock.System.now().toString()
}
