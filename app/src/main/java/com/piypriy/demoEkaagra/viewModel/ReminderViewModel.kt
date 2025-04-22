package com.piypriy.demoEkaagra.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.piypriy.demoEkaagra.model.ReminderItem

class ReminderViewModel : ViewModel() {

    // Master reminder list
    val reminders = mutableStateListOf(
        ReminderItem("Meditation"),
        ReminderItem("Exercise"),
        ReminderItem("Water"),
        ReminderItem("Medicine"),
        ReminderItem("Study"),
        ReminderItem("Hobby"),
        ReminderItem("Music")
    )

    // State for toggle (on/off)
    val toggleStates = mutableStateMapOf<String, Boolean>()

    // State for time per reminder
    val reminderTimes = mutableStateMapOf<String, String>()

    // Get top 3 active reminders (sorted by most recently toggled on)
    fun getTopActiveReminders(): List<Pair<String, String>> {
        return reminders
            .filter { toggleStates[it.title] == true && reminderTimes.containsKey(it.title) }
            .take(3)
            .map { it.title to (reminderTimes[it.title] ?: "Not Set") }
    }
}
