package io.github.loskovdm.timetracker.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.mp.KoinPlatform.getKoin

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<TimeTrackerDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("timetracker.db")
    return Room.databaseBuilder<TimeTrackerDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<TimeTrackerDatabase> {
    val context: Context = getKoin().get()
    return getDatabaseBuilder(context)
}