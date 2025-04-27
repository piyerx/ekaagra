// Updated AppControlScreen.kt (Now shows active timers)
package com.piypriy.demoEkaagra.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piypriy.demoEkaagra.viewModel.AppTimerViewModel
import java.util.Calendar


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
    var dialogVisible by remember { mutableStateOf(false) }
    var selectedApp by remember { mutableStateOf<InstalledApp?>(null) }
    var showDurationDialog by remember { mutableStateOf(false) }

    val timers by timerViewModel.appTimersFlow.collectAsState(initial = null)

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
                val appTimer = timers?.timersList?.find { it.appName == app.name }
                val timerText = appTimer?.let {
                    if (it.mode == "TIMER") {
                        "Timer: ${it.durationMinutes / 60}h ${it.durationMinutes % 60}m"
                    } else {
                        "Allowed: ${it.startHour}:${it.startMinute} - ${it.endHour}:${it.endMinute}"
                    }
                }
                AppRow(app, onClick = {
                    selectedApp = app
                    dialogVisible = true
                }, timerInfo = timerText)
            }
        }

        if (dialogVisible && selectedApp != null) {
            SetTimerDialog(
                context = context,
                appPackageName = selectedApp!!.name,
                onDismiss = { dialogVisible = false },
                timerViewModel = timerViewModel,
                onShowDurationPicker = { showDurationDialog = true }
            )
        }

        if (showDurationDialog && selectedApp != null) {
            DurationPickerDialog(
                onDismiss = { showDurationDialog = false },
                onDurationSelected = { minutes ->
                    timerViewModel.saveAppTimer(
                        appName = selectedApp!!.name,
                        mode = "TIMER",
                        duration = minutes
                    )
                    showDurationDialog = false
                }
            )
        }
    }
}

@Composable
fun AppRow(app: InstalledApp, onClick: () -> Unit, timerInfo: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Android,
                contentDescription = app.name,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = app.name, style = MaterialTheme.typography.bodyLarge)
        }
        if (timerInfo != null) {
            Text(
                text = timerInfo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 52.dp)
            )
        }
    }
}

@Composable
fun SetTimerDialog(
    context: Context,
    appPackageName: String,
    onDismiss: () -> Unit,
    timerViewModel: AppTimerViewModel,
    onShowDurationPicker: () -> Unit
) {
    var selectedOption by remember { mutableStateOf("App Timer") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                showTimePicker(
                    context = context,
                    appPackageName = appPackageName,
                    option = selectedOption,
                    timerViewModel = timerViewModel,
                    onShowDurationPicker = onShowDurationPicker
                )
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
    timerViewModel: AppTimerViewModel,
    onShowDurationPicker: () -> Unit
) {
    val calendar = Calendar.getInstance()

    if (option == "App Timer") {
        onShowDurationPicker()
    } else if (option == "Time Range") {
        TimePickerDialog(
            context,
            { _, startHour, startMinute ->
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

@Composable
fun DurationPickerDialog(
    onDismiss: () -> Unit,
    onDurationSelected: (Int) -> Unit
) {
    val options = listOf(15, 30, 45, 60, 90, 120, 180, 240, 300)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        title = { Text("Select Duration") },
        text = {
            Column {
                options.forEach { minutes ->
                    TextButton(onClick = {
                        onDurationSelected(minutes)
                    }) {
                        Text(if (minutes >= 60) "${minutes / 60}h ${minutes % 60}m" else "$minutes min")
                    }
                }
            }
        }
    )
}

// Dummy model

data class InstalledApp(val name: String)