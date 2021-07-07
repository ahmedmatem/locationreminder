package com.udacity.project4

import android.content.Context
import androidx.room.Room
import com.udacity.project4.locationreminders.data.DefaultDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository

object ServiceLocator {
    private var database: RemindersDatabase? = null

    @Volatile
    var dataSource: ReminderDataSource? = null

    fun provideReminderDataSource(context: Context): ReminderDataSource {
        synchronized(this) {
            return dataSource ?: createDefaultDataSource(context)
        }
    }

    private fun createDefaultDataSource(context: Context): ReminderDataSource {
        val newDataSource = DefaultDataSource(createLocalRepository(context))
        dataSource = newDataSource
        return newDataSource
    }

    private fun createLocalRepository(context: Context): RemindersLocalRepository {
        val database = database ?: createLocalDatabase(context)
        return RemindersLocalRepository(database.reminderDao())
    }

    private fun createLocalDatabase(context: Context): RemindersDatabase {
        val result = Room.databaseBuilder(
            context,
            RemindersDatabase::class.java,
            "locationReminders.db"
        ).build()
        database = result
        return result
    }
}