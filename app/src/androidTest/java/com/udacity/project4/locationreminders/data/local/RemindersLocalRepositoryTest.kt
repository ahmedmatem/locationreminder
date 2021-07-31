package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt

    private lateinit var database: RemindersDatabase
    private lateinit var localRepository: RemindersLocalRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localRepository = RemindersLocalRepository(
            database.reminderDao(),
            Dispatchers.Main
        )
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun saveReminder_getReminderById() = runBlocking {
        // Given - a new reminder saved in database
        val newReminder = ReminderDTO(
            "title",
            "description",
            "location",
            0.0,
            0.0
        )
        localRepository.saveReminder(newReminder)

        // When - reminder gets by id
        val loaded = localRepository.getReminder(newReminder.id)

        // Then - same reminder is retrieved
        loaded as Result.Success
        assertThat(loaded.data, `is`(newReminder))
        assertThat(loaded.data.title, `is`(newReminder.title))
        assertThat(loaded.data.description, `is`(newReminder.description))
        assertThat(loaded.data.location, `is`(newReminder.location))
        assertThat(loaded.data.latitude, `is`(newReminder.latitude))
        assertThat(loaded.data.longitude, `is`(newReminder.longitude))
    }

    @Test
    fun updateReminder_getReminderById() = runBlocking {
        // Given - a new reminder saved in database and updated
        val oldReminder = ReminderDTO(
            "title",
            "description",
            "location",
            0.0,
            0.0
        )
        localRepository.saveReminder(oldReminder)

        val newReminder = ReminderDTO(
            "new_title",
            "new_description",
            "new_location",
            1.0,
            1.0,
            oldReminder.id
        )
        // update
        localRepository.saveReminder(newReminder)

        // When - reminder gets by id
        val loaded = localRepository.getReminder(oldReminder.id)

        // Then - same reminder is retrieved
        loaded as Result.Success
        assertThat(loaded.data, `is`(newReminder))
        assertThat(loaded.data.title, `is`(newReminder.title))
        assertThat(loaded.data.description, `is`(newReminder.description))
        assertThat(loaded.data.location, `is`(newReminder.location))
        assertThat(loaded.data.latitude, `is`(newReminder.latitude))
        assertThat(loaded.data.longitude, `is`(newReminder.longitude))
    }
}