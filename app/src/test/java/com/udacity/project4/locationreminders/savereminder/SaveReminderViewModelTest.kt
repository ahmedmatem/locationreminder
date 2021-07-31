package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import org.hamcrest.MatcherAssert.assertThat

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    //TODO: provide testing to the SaveReminderView and its live data objects

    // Subject under test
    private lateinit var _viewModel: SaveReminderViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        _viewModel = SaveReminderViewModel(
            getApplicationContext(),
            FakeDataSource()
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun onClear_entryData_setNull() {
        // Given a view model and no nullable entries
        _viewModel.reminderTitle.value = "Title"
        _viewModel.reminderDescription.value = "Description"
        _viewModel.reminderSelectedLocationStr.value = "reminderSelectedLocationStr"
        _viewModel.selectedPOI.value = PointOfInterest(LatLng(0.0, 0.0), "", "")
        _viewModel.latitude.value = 0.0
        _viewModel.longitude.value = 0.0

        // When clear entry
        _viewModel.onClear()

        // Then assert that all properties are null
        val reminderTitle = _viewModel.reminderTitle.getOrAwaitValue()
        val reminderDescription = _viewModel.reminderDescription.getOrAwaitValue()
        val reminderSelectedLocationStr = _viewModel.reminderSelectedLocationStr.getOrAwaitValue()
        val selectedPOI = _viewModel.selectedPOI.getOrAwaitValue()
        val latitude = _viewModel.latitude.getOrAwaitValue()
        val longitude = _viewModel.longitude.getOrAwaitValue()

        assertThat(reminderTitle, `is`(nullValue()))
        assertThat(reminderDescription, `is`(nullValue()))
        assertThat(reminderSelectedLocationStr, `is`(nullValue()))
        assertThat(selectedPOI, `is`(nullValue()))
        assertThat(latitude, `is`(nullValue()))
        assertThat(longitude, `is`(nullValue()))
    }

    @Test
    fun validateEntryData_emptyTitle_setShowSnackBarIntAndReturnFalse() {
        // Given a fresh view model and entry with empty title
        val emptyTitleEntry = ReminderDataItem(
            "", "description", "location", 0.0, 0.0
        )

        // When validate entry
        val valid = _viewModel.validateEnteredData(emptyTitleEntry)

        // Then showSnackBarInt event is triggered
        val value = _viewModel.showSnackBarInt.getOrAwaitValue()

        assertThat(value, not(nullValue()))
        assert(!valid)
    }

    @Test
    fun validateEntryData_nullTitle_setShowSnackBarIntAndReturnFalse() {
        // Given a fresh view model and entry with null title
        val nullTitleEntry = ReminderDataItem(
            null, "description", "location", 0.0, 0.0
        )

        // When validate entry
        val valid = _viewModel.validateEnteredData(nullTitleEntry)

        // Then showSnackBarInt event is triggered
        val value = _viewModel.showSnackBarInt.getOrAwaitValue()

        assertThat(value, not(nullValue()))
        assert(!valid)
    }

    @Test
    fun validateEntryData_emptyLocation_setShowSnackBarIntAndReturnFalse() {
        // Given a fresh view model and entry with empty location
        val emptyLocationEntry = ReminderDataItem(
            "title", "description", "", 0.0, 0.0
        )

        // When validate entry
        val valid = _viewModel.validateEnteredData(emptyLocationEntry)

        // Then showSnackBarInt event is triggered
        val value = _viewModel.showSnackBarInt.getOrAwaitValue()

        assertThat(value, not(nullValue()))
        assert(!valid)
    }

    @Test
    fun validateEntryData_nullLocation_setShowSnackBarIntAndReturnFalse() {
        // Given a fresh view model and entry with null location
        val nullLocationEntry = ReminderDataItem(
            "title", "description", null, 0.0, 0.0
        )

        // When validate entry
        val valid = _viewModel.validateEnteredData(nullLocationEntry)

        // Then showSnackBarInt event is triggered
        val value = _viewModel.showSnackBarInt.getOrAwaitValue()

        assertThat(value, not(nullValue()))
        assert(!valid)
    }

}