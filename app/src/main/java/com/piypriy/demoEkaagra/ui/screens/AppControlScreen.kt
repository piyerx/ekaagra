package com.piypriy.demoEkaagra.ui.screens

import android.app.TimePickerDialog
import androidx.appcompat.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piypriy.demoEkaagra.viewmodel.AppTimerViewModel
import java.util.Calendar

data class InstalledApp(val name: String)

@Composable
fun AppControlScreen() {
    val apps = listOf(
        InstalledApp("Instagram"),
        InstalledApp("YouTube"),
        InstalledApp("WhatsApp"),
        InstalledApp("Spotify"),
        InstalledApp("Telegram"),
        InstalledApp("Snapchat"),
    )
    val timerViewModel: AppTimerViewModel = viewModel()
    val context = LocalContext.current
    var selectedApp by remember { mutableStateOf<InstalledApp?>(null) }
    var dialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "App Control",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(apps) { app ->
                AppRow(app) {
                    selectedApp = app
                    dialogVisible = true
                }
            }
        }

        if (dialogVisible && selectedApp != null) {
            SetTimerDialog(
                context = context,
                appPackageName = selectedApp!!.name,
                onDismiss = { dialogVisible = false },
                timerViewModel = timerViewModel
            )
        }
    }
}

@Composable
fun AppRow(app: InstalledApp, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Android,
            contentDescription = app.name,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = app.name, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SetTimerDialog(
    context: Context,
    appPackageName: String,
    onDismiss: () -> Unit,
    timerViewModel: AppTimerViewModel
) {
    var selectedOption by remember { mutableStateOf("App Timer") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                showTimePicker(context, appPackageName, selectedOption, timerViewModel)
                onDismiss()
            }) {
                Text("Set")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(text = "Set Timer for $appPackageName") },
        text = {
            Column {
                Text("Choose Option:")
                Spacer(modifier = Modifier.height(8.dp))
                listOf("App Timer", "Time Range").forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option }
                        )
                        Text(option)
                    }
                }
            }
        }
    )
}

fun showTimePicker(
    context: Context,
    appPackageName: String,
    option: String,
    timerViewModel: AppTimerViewModel
) {
    val calendar = Calendar.getInstance()

    if (option == "App Timer") {
        // Simple App Timer: Pick Hours and Minutes
        val durationOptions = (15..300 step 15).toList() // 15, 30, 45, ..., 300 mins (5 hours)
        val items = durationOptions.map { "${it / 60}h ${it % 60}m" }

        AlertDialog.Builder(context)
            .setTitle("Select Duration")
            .setItems(items.toTypedArray()) { _, which ->
                val selectedDuration = durationOptions[which]
                timerViewModel.saveAppTimer(
                    appName = appPackageName,
                    mode = "TIMER",
                    duration = selectedDuration
                )
            }
            .setNegativeButton("Cancel", null)
            .show()


    } else if (option == "Time Range") {
        // First: Pick Start Time
        TimePickerDialog(
            context,
            { _, startHour, startMinute ->

                // Then: Pick End Time
                TimePickerDialog(
                    context,
                    { _, endHour, endMinute ->
                        timerViewModel.saveAppTimer(
                            appName = appPackageName,
                            mode = "RANGE",
                            startHour = startHour,
                            startMinute = startMinute,
                            endHour = endHour,
                            endMinute = endMinute
                        )
                    },
                    startHour,
                    startMinute,
                    true
                ).show()

            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
