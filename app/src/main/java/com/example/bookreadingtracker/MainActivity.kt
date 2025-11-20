package com.example.bookreadingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.example.bookreadingtracker.core.ThemePreferences
import com.example.bookreadingtracker.ui.nav.AppNavGraph
import com.example.bookreadingtracker.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val context = LocalContext.current
            val scope: CoroutineScope = rememberCoroutineScope()
            val isDark by ThemePreferences.isDarkFlow(context).collectAsState(initial = false)

            AppTheme(dark = isDark) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavGraph(
                        isDark = isDark,
                        onToggleDark = { value -> scope.launch { ThemePreferences.setDark(context, value) } }
                    )
                }
            }
        }
    }
}
