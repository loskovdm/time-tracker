package io.github.loskovdm.timetracker.database.sync

import kotlin.time.Clock

/**
 * Placeholder owner until Supabase Auth is wired on the client.
 * Replace with the signed-in user's id when creating local rows.
 */
internal object SyncDefaults {
    const val LOCAL_OWNER_ID: String = "00000000-0000-0000-0000-000000000000"

    fun nowIso(): String = Clock.System.now().toString()
}
