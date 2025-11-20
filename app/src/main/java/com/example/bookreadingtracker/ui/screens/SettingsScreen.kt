package com.example.bookreadingtracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    nav: NavController,
    isDark: Boolean,
    onToggleDark: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 20).dp))
        Icon(Icons.Filled.Person, contentDescription = "Avatar", modifier = Modifier.size(80.dp).align(Alignment.CenterHorizontally))
        Text("UserName", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.CenterHorizontally))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("DarkMode", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.weight(1f))
            Switch(
                checked = isDark,
                onCheckedChange = onToggleDark
            )
        }
    }
}
