package com.piypriy.demoEkaagra.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

data class ReminderItem(
    val title: String,
    var isEnabled: Boolean = false,
    var time: String = "Not Set"
)

@Composable
fun LifestyleScreen() {
    val context = LocalContext.current

    // Predefined reminders
    val reminders = remember {
        mutableStateListOf(
            ReminderItem("Wake Up"),
            ReminderItem("Sleep"),
            ReminderItem("Workout"),
            ReminderItem("Study Time"),
            ReminderItem("Break"),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lifestyle",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(reminders) { reminder ->
                ReminderRow(reminder, context)
            }
        }
    }
}

@Composable
fun ReminderRow(reminder: ReminderItem, context: Context) {
    var isOn by remember { mutableStateOf(reminder.isEnabled) }
    var time by remember { mutableStateOf(reminder.time) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccessAlarm,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (isOn) {
                        showTimePickerDialog(context) { selectedTime ->
                            time = selectedTime
                            reminder.time = selectedTime
                        }
                    }
                }
        ) {
            Text(text = reminder.title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (isOn) "Time: $time" else "Disabled",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Switch(
            checked = isOn,
            onCheckedChange = {
                isOn = it
                reminder.isEnabled = it
            }
        )
    }
}

fun showTimePickerDialog(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
    }, hour, minute, true).show()
}
