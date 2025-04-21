package com.piypriy.demoEkaagra.ui.screens

import androidx.compose.ui.unit.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*

@Composable
fun AppControlScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "App Control", fontSize = 28.sp, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "TODO: List third-party apps, allow setting timers or allowed ranges.")
    }
}