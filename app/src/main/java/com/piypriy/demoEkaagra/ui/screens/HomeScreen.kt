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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piypriy.demoEkaagra.viewModel.ReminderViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0049b0))
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
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onSettingsClick() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Daily Screen Time (for now, mock value)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Screen Time"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Today’s screen time: 2h 36m",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Circular Progress – Day % Passed
        val progress = remember { 0.5f } // 50% for now
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                progress = { progress },
                strokeWidth = 6.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("${(progress * 100).toInt()}% of the day passed")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Overview Sections
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OverviewCard(
                title = "Dashboard",
                icon = Icons.Default.BarChart,
                description = "Top 3 most used apps today",
                onClick = onDashboardClick
            )

            OverviewCard(
                title = "App Control",
                icon = Icons.Default.Lock,
                description = "Apps with active timers",
                onClick = onAppControlClick
            )

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
                        color = Color.Gray
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        topReminders.forEach { (name, time) ->
                            Text(
                                text = "• $name at $time",
                                style = MaterialTheme.typography.bodySmall
                            )
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
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = description, fontSize = 14.sp)
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
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = description, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}