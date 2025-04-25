package com.piypriy.demoEkaagra.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.piypriy.demoEkaagra.viewmodel.AppTimerViewModel
import java.util.*

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
                appName = selectedApp!!.name,
                onDismiss = { dialogVisible = false }
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
                showTimePicker(context, app.packageName, selectedOption, timerViewModel)
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
        title = { Text(text = "Set Timer for $appName") },
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
    appName: String,
    option: String,
    timerViewModel: AppTimerViewModel
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            if (option == "App Timer") {
                val duration = selectedHour * 60 + selectedMinute
                timerViewModel.saveAppTimer(
                    appName = appName,
                    mode = "TIMER",
                    duration = duration
                )
            } else if (option == "Time Range") {
                timerViewModel.saveAppTimer(
                    appName = appName,
                    mode = "RANGE",
                    startHour = selectedHour,
                    startMinute = selectedMinute
                )
            }
        },
        hour,
        minute,
        true
    ).show()
}
