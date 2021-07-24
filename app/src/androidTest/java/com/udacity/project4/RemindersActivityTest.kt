package com.udacity.project4

import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.EspressoIdlingResource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers.hasEntry
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

@RunWith(AndroidJUnit4::class)
@LargeTest
//END TO END test to black box test the app
class RemindersActivityTest :
    AutoCloseKoinTest() {// Extended Koin Test - embed autoclose @after method to close Koin after every test

    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
//        stopKoin()//stop the original app koin
//        appContext = getApplicationContext()
//        val myModule = module {
//            viewModel {
//                RemindersListViewModel(
//                    appContext,
//                    get() as ReminderDataSource
//                )
//            }
//            single {
//                SaveReminderViewModel(
//                    appContext,
//                    get() as ReminderDataSource
//                )
//            }
//            single { RemindersLocalRepository(get()) as ReminderDataSource }
//            single { LocalDB.createRemindersDao(appContext) }
//        }
//        //declare a new koin module
//        startKoin {
//            modules(listOf(myModule))
//        }
        //Get our real repository
        repository = get()

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

//    TODO: add End to End testing to the app

    @Test
    fun addReminder() = runBlockingTest {
        val reminderDto = ReminderDTO(
            "title",
            "description",
            "location",
            0.0,
            0.0
        )

        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)

        // click on the add reminder fab
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).perform(replaceText(reminderDto.title))
        onView(withId(R.id.reminderDescription)).perform(replaceText(reminderDto.description))
//        onView(withId(R.id.selectedLocation)).perform(replaceText("location_1"))

        // click on save fab button to add reminder
        onView(withId(R.id.saveReminder)).perform(click())

        // verify that reminder displayed on screen
        onData(allOf(`is`(instanceOf(Map::class.java)), `is`(withText(reminderDto.title))))
            .check(matches(isDisplayed()))

        activityScenario.close()
    }

}
