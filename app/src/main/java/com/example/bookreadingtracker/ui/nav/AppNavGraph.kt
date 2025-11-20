package com.example.bookreadingtracker.ui.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.bookreadingtracker.ui.screens.*

@Composable
fun AppNavGraph(
    isDark: Boolean,
    onToggleDark: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) {
        NavHost(
            navController = navController,
            startDestination = Destinations.HOME,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Destinations.HOME) { HomeScreen(navController) }
            composable(Destinations.DISCOVER) { DiscoverScreen(navController) }
            composable(Destinations.SHELF) { ShelfScreen(navController) }
            composable(Destinations.ANALYTICS) { AnalyticsScreen(navController) }
            composable(Destinations.SETTINGS) {
                SettingsScreen(
                    nav = navController,
                    isDark = isDark,
                    onToggleDark = onToggleDark
                )
            }
            composable(
                route = Destinations.DETAIL,
                arguments = listOf(navArgument("bookId") { defaultValue = "" })
            ) {
                BookDetailScreen(navController)
            }
        }
    }
}

@Composable
fun BottomBar(nav: NavHostController) {
    var selected by remember { mutableStateOf(0) }
    val items = listOf(
        Destinations.HOME to "Home",
        Destinations.DISCOVER to "Discover",
        Destinations.SHELF to "Shelf",
        Destinations.ANALYTICS to "Analytics",
        Destinations.SETTINGS to "Settings"
    )
    NavigationBar {
        items.forEachIndexed { i, (route, label) ->
            NavigationBarItem(
                selected = selected == i,
                onClick = {
                    selected = i
                    nav.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                    }
                },
                icon = {
                    when (label) {
                        "Home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "Discover" -> Icon(Icons.Filled.Search, contentDescription = "Discover")
                        "Shelf" -> Icon(Icons.Filled.LibraryBooks, contentDescription = "Shelf")
                        "Analytics" -> Icon(Icons.Filled.BarChart, contentDescription = "Analytics")
                        "Settings" -> Icon(Icons.Filled.Settings, contentDescription = "Settings")
                        else -> Text(label.take(1))
                    }
                },
                label = { Text(label.lowercase()) }
            )
        }
    }
}
