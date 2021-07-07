package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDataSource(
    private val localRepository: RemindersLocalRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return localRepository.getReminders()
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        localRepository.saveReminder(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        return localRepository.getReminder(id)
    }

    override suspend fun deleteAllReminders() {
        localRepository.deleteAllReminders()
    }
}