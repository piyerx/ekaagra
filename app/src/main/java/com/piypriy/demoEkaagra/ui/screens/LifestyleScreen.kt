package com.piypriy.demoEkaagra.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piypriy.demoEkaagra.viewModel.ReminderViewModel

import java.util.*

@Composable
fun LifestyleScreen(
    viewModel: ReminderViewModel = viewModel()
) {
    val reminders by viewModel.reminders.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lifestyle Reminders",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(reminders) { reminder ->
                ReminderCard(
                    context = context,
                    name = reminder.name,
                    isEnabled = reminder.isEnabled,
                    time = reminder.time,
                    onToggle = { viewModel.toggleReminder(reminder.name) },
                    onTimeChange = { newTime -> viewModel.setReminderTime(reminder.name, newTime) }
                )
            }
        }
    }
}

@Composable
fun ReminderCard(
    context: Context,
    name: String,
    isEnabled: Boolean,
    time: String,
    onToggle: () -> Unit,
    onTimeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 70.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = name, style = MaterialTheme.typography.titleMedium)
                if (isEnabled && time.isNotEmpty()) {
                    Text(
                        text = "Time: $time",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isEnabled) {
                    Button(
                        onClick = {
                            showTimePickerDialog(context, time, onTimeChange)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Set Time")
                    }
                }
                Switch(checked = isEnabled, onCheckedChange = { onToggle() })
            }
        }
    }
}

fun showTimePickerDialog(
    context: Context,
    currentTime: String,
    onTimeSelected: (String) -> Unit
) {
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)

    TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        hour,
        minute,
        true
    ).show()
}
