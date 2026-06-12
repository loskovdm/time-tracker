package io.github.loskovdm.timetracker.database.repository

import io.github.loskovdm.domain.auth.GuestUserIds
import io.github.loskovdm.domain.repository.GuestDataRepository
import io.github.loskovdm.timetracker.database.dao.ProjectDao
import io.github.loskovdm.timetracker.database.dao.TaskDao
import io.github.loskovdm.timetracker.database.dao.TimeEntryDao

internal class GuestDataRepositoryImpl(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    private val timeEntryDao: TimeEntryDao,
) : GuestDataRepository {
    override suspend fun hasGuestData(): Boolean {
        val guestId = GuestUserIds.LOCAL
        return projectDao.countGuestProjects(guestId) > 0 ||
            taskDao.countGuestTasks(guestId) > 0 ||
            timeEntryDao.countGuestTimeEntries(guestId) > 0
    }

    override suspend fun migrateToUser(userId: String) {
        val guestId = GuestUserIds.LOCAL
        val projects = projectDao.getProjectsByUserId(guestId)
        val tasks = taskDao.getTasksByUserId(guestId)
        val timeEntries = timeEntryDao.getTimeEntriesByUserId(guestId)

        for (entry in timeEntries) {
            timeEntryDao.deleteTimeEntry(entry)
        }
        for (task in tasks) {
            taskDao.deleteTask(task)
        }
        for (project in projects) {
            projectDao.deleteProject(project)
        }

        for (project in projects) {
            projectDao.insertProject(project.copy(userId = userId))
        }
        for (task in tasks) {
            taskDao.insertTask(task)
        }
        for (entry in timeEntries) {
            timeEntryDao.insertTimeEntry(entry.copy(userId = userId))
        }
    }

    override suspend fun clearAll() {
        timeEntryDao.deleteAllTimeEntries()
        taskDao.deleteAllTasks()
        projectDao.deleteAllProjects()
    }
}
