package com.piypriy.demoEkaagra.ui.screens

import androidx.compose.ui.unit.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*

@Composable
fun LifestyleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Lifestyle", fontSize = 28.sp, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "TODO: Show list of reminders, allow toggling, fixed list only.")
    }
}