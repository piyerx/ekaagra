package com.piypriy.demoEkaagra.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.piypriy.demoEkaagra.model.ReminderItem

class ReminderViewModel : ViewModel() {

    val reminders = mutableStateListOf(
        ReminderItem("Meditation", isEnabled = false, time = ""),
        ReminderItem("Exercise", isEnabled = false, time = ""),
        ReminderItem("Water", isEnabled = false, time = ""),
        ReminderItem("Medicine", isEnabled = false, time = ""),
        ReminderItem("Study", isEnabled = false, time = ""),
        ReminderItem("Hobby", isEnabled = false, time = ""),
        ReminderItem("Music", isEnabled = false, time = "")
    )

    fun toggleReminder(reminderName: String) {
        val index = reminders.indexOfFirst { it.name == reminderName }
        if (index != -1) {
            val current = reminders[index]
            reminders[index] = current.copy(isEnabled = !current.isEnabled)
        }
    }

    fun setReminderTime(reminderName: String, newTime: String) {
        val index = reminders.indexOfFirst { it.name == reminderName }
        if (index != -1) {
            val current = reminders[index]
            reminders[index] = current.copy(time = newTime)
        }
    }

    fun getTopActiveReminders(): List<Pair<String, String>> {
        return reminders
            .filter { it.isEnabled && it.time.isNotEmpty() }
            .take(3)
            .map { it.name to it.time }
    }
}

