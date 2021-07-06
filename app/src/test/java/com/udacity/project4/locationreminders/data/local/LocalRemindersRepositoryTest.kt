package com.udacity.project4.locationreminders.data.local

import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.internal.matchers.Equals
import java.lang.ClassCastException

class LocalRemindersRepositoryTest {
    private val reminderDTO1 = ReminderDTO("Title1", "Description1", "Location1", 1.0, 1.0, "1")
    private val reminderDTO2 = ReminderDTO("Title2", "Description2", "Location2", 2.0, 2.0, "2")
    private val reminderDTO3 =
        ReminderDTO("Title3", "Description3", "Location3", 3.0, 3.0, id = "3")
    private val reminderDTO4 =
        ReminderDTO("Title4", "Description4", "Location4", 4.0, 4.0, id = "4")
    private val reminderDTO5 =
        ReminderDTO("Title5", "Description5", "Location5", 5.0, 5.0, id = "5")
    private val fakeReminders: MutableList<ReminderDTO> =
        mutableListOf(reminderDTO1, reminderDTO2, reminderDTO3, reminderDTO4, reminderDTO5)

    // Subject under test
    private lateinit var reminderRepository: ReminderDataSource

    @Before
    fun createRepository() {
        reminderRepository = FakeDataSource(fakeReminders)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getReminders_requestAllRemindersFromLocalDataSource() = runBlockingTest {
        // When reminders requested form local repository
        val allReminders = reminderRepository.getReminders() as Result.Success

        // Then reminders are loaded from the local data source
        assertThat(allReminders.data, IsEqual(fakeReminders))
    }

    @Test
    fun deleteAllReminders_deleteAllRemindersFromLocalDataSource() = runBlockingTest {
        // When request deleting all reminders from local repository
        reminderRepository.deleteAllReminders()

        // Then all reminders will be deleted from local data source
        assertThat(fakeReminders.isEmpty(), `is`(true))
    }

    @Test
    fun getReminder_requestAReminderByIdFromLocalDataSource() = runBlockingTest {
        // When request a reminder by id from local repository
        val id = "11"
        val reminder = reminderRepository.getReminder(id)
        try {
            reminder as Result.Success
            // Then a reminder with given id is returned from local data source
            assertThat(reminder.data, IsEqual(fakeReminders[0]))
        } catch (ex: ClassCastException) {
            reminder as Result.Error
            assertThat(reminder.message, `is`("No reminder found with given id: $id"))
        }
    }

    @Test
    fun saveReminder() = runBlockingTest {
        val id = "newId"
        val reminderToSave = ReminderDTO(
            "sTitle",
            "sDescription",
            "sLocation",
            0.0,
            0.0,
            id
        )
        // When request saving of new reminder from repository
        reminderRepository.saveReminder(reminderToSave)
        // get all reminders and and check if contains the new saved reminder
        val allReminders = reminderRepository.getReminders() as Result.Success
        // Then new reminder is added to local data source
        assertThat(allReminders.data.find { it.id == id }, IsEqual(reminderToSave))
    }
}