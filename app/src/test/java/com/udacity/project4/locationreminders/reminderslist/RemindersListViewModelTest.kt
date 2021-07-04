package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.utils.toReminderDataItemList
import org.hamcrest.MatcherAssert.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects

    private val reminderDTO1 = ReminderDTO("Title1", "Description1", "Location1", 1.0, 1.0)
    private val reminderDTO2 = ReminderDTO("Title2", "Description2", "Location2", 2.0, 2.0)
    private val reminderDTO3 = ReminderDTO("Title3", "Description3", "Location3", 3.0, 3.0)
    private val reminderDTO4 = ReminderDTO("Title4", "Description4", "Location4", 4.0, 4.0)
    private val reminderDTO5 = ReminderDTO("Title5", "Description5", "Location5", 5.0, 5.0)
    private val fakeReminders: MutableList<ReminderDTO> =
        mutableListOf(reminderDTO1, reminderDTO2, reminderDTO3, reminderDTO4, reminderDTO5)

    // Subject under test
    private lateinit var _viewModel: RemindersListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        _viewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            FakeDataSource(fakeReminders)
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun loadReminders_requestAllRemindersFromLocalDb() {
        // When all reminders are required from local db
        _viewModel.loadReminders()

        // Then view model RemindersList live data wil be set
        val remindersList = _viewModel.remindersList.getOrAwaitValue()

        assertThat(remindersList, IsEqual(fakeReminders.toReminderDataItemList()))
    }
}