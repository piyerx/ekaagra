package com.piypriy.demoEkaagra.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piypriy.demoEkaagra.R

data class AppUsageItem(val name: String, val usageTime: String, val icon: ImageVector)

@Composable
fun DashboardScreen() {
    val dummyUsageData = listOf(
        AppUsageItem("Instagram", "2h 15m", Icons.Default.Android),
        AppUsageItem("YouTube", "1h 45m", Icons.Default.Android),
        AppUsageItem("Chrome", "1h 30m", Icons.Default.Android),
        AppUsageItem("WhatsApp", "1h 10m", Icons.Default.Android),
        AppUsageItem("Spotify", "55m", Icons.Default.Android),
        AppUsageItem("Reddit", "50m", Icons.Default.Android),
        AppUsageItem("Twitter", "45m", Icons.Default.Android),
        AppUsageItem("Telegram", "30m", Icons.Default.Android),
        AppUsageItem("Snapchat", "25m", Icons.Default.Android),
        AppUsageItem("Facebook", "20m", Icons.Default.Android),
    )

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
            // Title
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Total Screen Time
            Text(
                text = "Total Screen Time Today",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "5h 25m", // Placeholder
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Day navigation row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Not functional yet */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous Day")
                }
                Text("Today", fontSize = 18.sp)
                IconButton(onClick = { /* Not functional yet */ }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next Day")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Top 10 Apps Used
            Text(
                text = "Top 10 Most Used Apps",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn {
                items(dummyUsageData) { app ->
                    AppUsageItemView(app)
                }
            }
        }
    }
}

@Composable
fun AppUsageItemView(app: AppUsageItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = app.icon,
            contentDescription = app.name,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = app.name, style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text(text = app.usageTime, style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
    }
}
