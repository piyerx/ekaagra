package com.piypriy.demoEkaagra.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.piypriy.demoEkaagra.ReminderItem
import com.piypriy.demoEkaagra.ReminderList
import com.piypriy.demoEkaagra.datastore.reminderDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers


class ReminderViewModel(application: Application) : AndroidViewModel(application) {


    val reminders = mutableStateListOf(
        ReminderItem.newBuilder().setName("Meditation").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Exercise").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Water").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Medicine").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Study").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Hobby").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Music").setIsEnabled(false).setTime("").build()
    )


    fun toggleReminder(reminderName: String) {
        val index = reminders.indexOfFirst { it.name == reminderName }
        if (index != -1) {
            val current = reminders[index]
            reminders[index] = current.toBuilder()
                .setIsEnabled(!current.isEnabled)
                .build()
            saveRemindersToDataStore()
        }
    }


    fun setReminderTime(reminderName: String, newTime: String) {
        val index = reminders.indexOfFirst { it.name == reminderName }
        if (index != -1) {
            val current = reminders[index]
            reminders[index] = current.toBuilder()
                .setTime(newTime)
                .build()
            saveRemindersToDataStore()
        }
    }

    private fun saveRemindersToDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            getApplication<Application>().reminderDataStore.updateData { current ->
                current.toBuilder()
                    .clearReminders()
                    .addAllReminders(reminders)
                    .build()
            }
        }
    }



    fun getTopActiveReminders(): List<Pair<String, String>> {
        return reminders
            .filter { it.isEnabled && it.time.isNotEmpty() }
            .take(3)
            .map { it.name to it.time }
    }
}

