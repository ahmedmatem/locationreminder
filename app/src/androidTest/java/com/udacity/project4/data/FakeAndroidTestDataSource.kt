package com.udacity.project4.data

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeAndroidTestDataSource(
    var reminders: MutableList<ReminderDTO> = mutableListOf(),
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        // TODO("Return the reminders")
        return Result.Success(reminders)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        // TODO("save the reminder")
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        // TODO("return the reminder with the id")
        val reminder = reminders?.firstOrNull() {
            it.id == id
        }
        return if (reminder != null) {
            Result.Success(reminder)
        } else {
            Result.Error("No reminder found with given id: $id")
        }
    }

    override suspend fun deleteAllReminders() {
        // TODO("delete all the reminders")
        reminders.clear()
    }
}