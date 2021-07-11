package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt

    private lateinit var database: RemindersDatabase

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertReminderAndGetById() = runBlockingTest {
        // Given - insert a reminder
        val reminder = ReminderDTO(
            "title",
            "description",
            "location",
            0.0,
            0.0
        )
        database.reminderDao().saveReminder(reminder)

        // When - get reminder by id
        val loaded = database.reminderDao().getReminderById(reminder.id)

        // Then - the loaded data contains the expected values
        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(reminder.id))
        assertThat(loaded.title, `is`(reminder.title))
        assertThat(loaded.description, `is`(reminder.description))
        assertThat(loaded.location, `is`(reminder.location))
        assertThat(loaded.latitude, `is`(reminder.latitude))
        assertThat(loaded.longitude, `is`(reminder.longitude))
    }

    @Test
    fun updateReminderAndGetById() = runBlockingTest {
        // Given - save/insert and update a reminder
        val reminder = ReminderDTO(
            "title",
            "description",
            "location",
            0.0,
            0.0
        )
        database.reminderDao().saveReminder(reminder)

        val newReminder = ReminderDTO(
            "new_title",
            "new_description",
            "new_location",
            1.0,
            1.0,
            reminder.id
        )
        // update
        database.reminderDao().saveReminder(newReminder)

        // When - get reminder by id
        val loaded = database.reminderDao().getReminderById(newReminder.id)

        // Then - the loaded data contains the expected values
        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(newReminder.id))
        assertThat(loaded.title, `is`(newReminder.title))
        assertThat(loaded.description, `is`(newReminder.description))
        assertThat(loaded.location, `is`(newReminder.location))
        assertThat(loaded.latitude, `is`(newReminder.latitude))
        assertThat(loaded.longitude, `is`(newReminder.longitude))
    }

    @Test
    fun deleteAllReminders_getReminders_sizeZero() = runBlockingTest {
        // Given - reminders
        val reminder1 = ReminderDTO(
            "title_1",
            "description_1",
            "location_1",
            1.0,
            1.0
        )
        val reminder2 = ReminderDTO(
            "title_2",
            "description_2",
            "location_2",
            2.0,
            2.0
        )
        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)

        // When - get all reminders
        val allReminders = database.reminderDao().getReminders()

        // Then - assert that all reminders count is as expected
        assertThat(2, `is`(allReminders.size))

        // When - delete all reminders and getReminders
        database.reminderDao().deleteAllReminders()
        val loaded = database.reminderDao().getReminders()

        // Then - assert that the loaded reminders count is 0
        assertThat(0, `is`(loaded.size))
    }
}