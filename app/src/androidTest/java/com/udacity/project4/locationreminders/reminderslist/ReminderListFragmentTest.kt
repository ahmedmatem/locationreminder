package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.data.FakeAndroidTestDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

    private val reminder1 = ReminderDTO("t1", "d1", "l1", .0, .0)
    private val reminder2 = ReminderDTO("t2", "d2", "l2", .0, .0)
    private val reminder3 = ReminderDTO("t3", "d3", "l3", .0, .0)
    private val reminder4 = ReminderDTO("t4", "d4", "l4", .0, .0)
    private val reminders = mutableListOf<ReminderDTO>(
        reminder1,
        reminder2,
        reminder3,
        reminder4
    )

    private lateinit var viewModel: RemindersListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        stopKoin() // stop app original koin
        val myTestModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                RemindersListViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            //Declare singleton definitions to be later injected using by inject()
            single {
                //This view model is declared singleton to be used across multiple fragments
                SaveReminderViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
//            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { FakeAndroidTestDataSource(reminders) as ReminderDataSource }
            single { LocalDB.createRemindersDao(getApplicationContext()) }
        }

        startKoin {
            androidContext(getApplicationContext())
            modules(listOf(myTestModule))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    //    TODO: test the navigation of the fragments.
    //    TODO: test the displayed data on the UI.
    //    TODO: add testing for the error messages.

    @Test
    fun reminderList_DisplayedInUi() = runBlocking {
        // Given a list of reminders
        // viewModel.loadReminders()
//        val v = viewModel.remindersList.getOrAwaitValue()

        // When - Reminder list fragment launched to display items in the list
        launchFragmentInContainer<ReminderListFragment>(null, R.style.AppTheme)
        Thread.sleep(2000)
    }

}