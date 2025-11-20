package com.example.bookreadingtracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.bookreadingtracker.domain.usecase.GetAnalytics
import com.example.bookreadingtracker.domain.usecase.BookStat
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import coil.compose.AsyncImage

data class AnalyticsUiState(
    val totalMinutes: Int = 0,
    val sessionsCount: Int = 0,
    val perBook: List<BookStat> = emptyList()
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getAnalytics: GetAnalytics
) : ViewModel() {
    val uiState: StateFlow<AnalyticsUiState> =
        getAnalytics().map { AnalyticsUiState(it.totalMinutes, it.sessionsCount, it.perBook) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsUiState())
}

@Composable
fun AnalyticsScreen(nav: NavController, vm: AnalyticsViewModel = hiltViewModel()) {
    val s = vm.uiState.collectAsState().value
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 20).dp))
        Text("Analytics", style = MaterialTheme.typography.headlineMedium)
        Divider(Modifier.padding(vertical = 8.dp))
        Text("Total Reading Time: ${s.totalMinutes} min", style = MaterialTheme.typography.titleLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 12.dp)) {
            item {
                Row {
                    Text("icon", style = MaterialTheme.typography.labelLarge, modifier = Modifier.width(48.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("name", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    Text("time", style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.End, modifier = Modifier.width(80.dp))
                }
                Divider(Modifier.padding(vertical = 8.dp))
            }
            items(s.perBook) { item ->
                Row {
                    AsyncImage(model = item.coverUrl, contentDescription = item.title, modifier = Modifier.width(48.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(item.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    Text("${item.minutes} min", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.End, modifier = Modifier.width(80.dp))
                }
            }
        }
    }
}
