package com.piypriy.demoEkaagra.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.piypriy.demoEkaagra.ReminderItem
import com.piypriy.demoEkaagra.datastore.reminderDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*


class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val _reminders = MutableStateFlow<List<ReminderItem>>(listOf(
        ReminderItem.newBuilder().setName("Meditation").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Exercise").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Water").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Medicine").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Study").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Hobby").setIsEnabled(false).setTime("").build(),
        ReminderItem.newBuilder().setName("Music").setIsEnabled(false).setTime("").build()
    ))

    val reminders: StateFlow<List<ReminderItem>> = _reminders.asStateFlow()

    init {
        loadRemindersFromDataStore()
    }

    private fun loadRemindersFromDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            val saved = getApplication<Application>().reminderDataStore.data.first()
            if (saved.remindersList.isNotEmpty()) {
                _reminders.value = saved.remindersList
            }
        }
    }

    private fun saveRemindersToDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            getApplication<Application>().reminderDataStore.updateData { current ->
                current.toBuilder()
                    .clearReminders()
                    .addAllReminders(_reminders.value)
                    .build()
            }
        }
    }

    fun toggleReminder(reminderName: String) {
        val updated = _reminders.value.map {
            if (it.name == reminderName) it.toBuilder().setIsEnabled(!it.isEnabled).build()
            else it
        }
        _reminders.value = updated
        saveRemindersToDataStore()
    }

    fun setReminderTime(reminderName: String, newTime: String) {
        val updated = _reminders.value.map {
            if (it.name == reminderName) it.toBuilder().setTime(newTime).build()
            else it
        }
        _reminders.value = updated
        saveRemindersToDataStore()
    }

    fun getTopActiveReminders(): List<Pair<String, String>> {
        return _reminders.value
            .filter { it.isEnabled && it.time.isNotEmpty() }
            .take(3)
            .map { it.name to it.time }
    }
}


