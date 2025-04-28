// FINAL Stable Version: AppControlScreen.kt
package com.piypriy.demoEkaagra.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piypriy.demoEkaagra.R
import com.piypriy.demoEkaagra.utils.AppInfo
import com.piypriy.demoEkaagra.utils.getThirdPartyApps
import com.piypriy.demoEkaagra.viewModel.AppTimerViewModel
import java.util.Calendar

@Composable
fun AppControlScreen(
    timerViewModel: AppTimerViewModel = viewModel()
) {
    val context = LocalContext.current
    val apps = remember { getThirdPartyApps(context) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedApp by remember { mutableStateOf<AppInfo?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ekaagrabackground),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "App Control",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(apps) { app ->
                    AppItemCard(app = app) {
                        selectedApp = app
                        showDialog = true
                    }
                }
            }
        }

        if (showDialog && selectedApp != null) {
            SetTimerDialog(
                context = context,
                appName = selectedApp!!.packageName,
                timerViewModel = timerViewModel,
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun AppItemCard(app: AppInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF003366)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = app.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun SetTimerDialog(
    context: Context,
    appName: String,
    timerViewModel: AppTimerViewModel,
    onDismiss: () -> Unit
) {
    var selectedOption by remember { mutableStateOf("App Timer") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Choose Control Type")
        },
        text = {
            Column {
                TextButton(onClick = { selectedOption = "App Timer" }) {
                    Text("App Timer")
                }
                TextButton(onClick = { selectedOption = "Set Range" }) {
                    Text("Set Range")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (selectedOption == "App Timer") {
                    showDurationPicker(context, appName, timerViewModel)
                } else {
                    showTimeRangePicker(context, appName, timerViewModel)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun showDurationPicker(context: Context, appPackageName: String, timerViewModel: AppTimerViewModel) {
    val timeOptions = (15..240 step 15).map { it.toString() }

    androidx.appcompat.app.AlertDialog.Builder(context)
        .setTitle("Select App Timer (minutes)")
        .setItems(timeOptions.toTypedArray()) { _, which ->
            val selectedMinutes = timeOptions[which].toInt()
            timerViewModel.saveAppTimer(
                appName = appPackageName,
                mode = "TIMER",
                duration = selectedMinutes
            )
        }
        .show()
}

fun showTimeRangePicker(context: Context, appPackageName: String, timerViewModel: AppTimerViewModel) {
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)

    TimePickerDialog(
        context,
        { _: TimePicker, startHour: Int, startMinute: Int ->
            TimePickerDialog(
                context,
                { _: TimePicker, endHour: Int, endMinute: Int ->
                    timerViewModel.saveAppTimer(
                        appName = appPackageName,
                        mode = "RANGE",
                        startHour = startHour,
                        startMinute = startMinute,
                        endHour = endHour,
                        endMinute = endMinute
                    )
                },
                hour,
                minute,
                true
            ).show()
        },
        hour,
        minute,
        true
    ).show()
}
