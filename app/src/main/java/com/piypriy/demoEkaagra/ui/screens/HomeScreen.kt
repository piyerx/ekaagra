package com.piypriy.demoEkaagra.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piypriy.demoEkaagra.viewModel.ReminderViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piypriy.demoEkaagra.R
import com.piypriy.demoEkaagra.viewModel.AppTimerViewModel


@Composable
fun HomeScreen(
    reminderViewModel: ReminderViewModel = viewModel(),
    onDashboardClick: () -> Unit,
    onAppControlClick: () -> Unit,
    onLifestyleClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val topReminders by remember(reminderViewModel.reminders.collectAsState()) {
        derivedStateOf { reminderViewModel.getTopActiveReminders() }
    }
    val appTimerViewModel: AppTimerViewModel = viewModel()
    val timers by appTimerViewModel.appTimersFlow.collectAsState(initial = null)
    val topTimedApps = timers?.timersList
        ?.take(3) // Take top 3
        ?.map { it.appName } ?: emptyList()



    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ekaagrabackground),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Row: Title + Profile
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ekaagra",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onSettingsClick() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Daily Screen Time (for now, mock value)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Screen Time",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Today’s screen time: 1h 12m",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Circular Progress – Day % Passed
            val progress = remember { 0.5f }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    strokeWidth = 8.dp,
                    color = Color.Cyan
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${(progress * 100).toInt()}% of the day passed",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Overview Sections
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OverviewCard(
                    title = "Dashboard",
                    icon = Icons.Default.BarChart,
                    description = "Top 3 most used apps today",
                    onClick = onDashboardClick
                )

                OverviewCardWithContent(
                    title = "App Control",
                    icon = Icons.Default.Lock,
                    description = "Apps with active timers",
                    onClick = onAppControlClick
                ) {
                    if (topTimedApps.isEmpty()) {
                        Text(
                            text = "No apps under control",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            topTimedApps.forEach { appName ->
                                Text(
                                    text = "• $appName",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }


                OverviewCardWithContent(
                    title = "Lifestyle",
                    icon = Icons.Default.Event,
                    description = "Upcoming reminders",
                    onClick = onLifestyleClick
                ) {
                    if (topReminders.isEmpty()) {
                        Text(
                            text = "No active reminders",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            topReminders.forEach { (name, time) ->
                                Text(
                                    text = "• $name at $time",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OverviewCard(
    title: String,
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF004AAD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                Text(text = description, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
fun OverviewCardWithContent(
    title: String,
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF004AAD)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                    Text(text = description, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
