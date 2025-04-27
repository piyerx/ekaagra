package com.piypriy.demoEkaagra

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.piypriy.demoEkaagra.ui.screens.HomeScreen
import com.piypriy.demoEkaagra.ui.screens.DashboardScreen
import com.piypriy.demoEkaagra.ui.screens.AppControlScreen
import com.piypriy.demoEkaagra.ui.screens.LifestyleScreen
import com.piypriy.demoEkaagra.ui.screens.SettingsScreen
import com.piypriy.demoEkaagra.ui.theme.EkaagraTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Need to fix this later, not working properly
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.setDecorFitsSystemWindows(false)
//        }
        setContent {
            EkaagraApp()
        }
    }
}

@Composable
fun EkaagraApp() {
    EkaagraTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        onDashboardClick = { navController.navigate("dashboard") },
                        onAppControlClick = { navController.navigate("appcontrol") },
                        onLifestyleClick = { navController.navigate("lifestyle") },
                        onSettingsClick = { navController.navigate("settings") }
                    )
                }
                composable("dashboard") { DashboardScreen() }
                composable("appcontrol") { AppControlScreen() }
                composable("lifestyle") { LifestyleScreen() }
                composable("settings") { SettingsScreen() }
            }
        }
    }
}
