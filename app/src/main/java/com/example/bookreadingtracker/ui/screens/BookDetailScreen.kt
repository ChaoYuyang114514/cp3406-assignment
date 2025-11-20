// File path: app/src/main/java/com/example/bookreadingtracker/ui/screens/BookDetailScreen.kt

package com.example.bookreadingtracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookreadingtracker.ui.screens.BookDetailViewModel

@Composable
fun BookDetailScreen(
    navController: NavController,
    vm: BookDetailViewModel = hiltViewModel()
) {
    val s by vm.uiState.collectAsState()
    val snackbarHostState = SnackbarHostState()

    when {
        s.isLoading -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }

        s.error != null -> {
            LaunchedEffect(s.error) { s.error?.let { snackbarHostState.showSnackbar(it) } }
        }

        else -> {
            val b = s.book!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 20).dp))
                SnackbarHost(hostState = snackbarHostState)
                Row(verticalAlignment = Alignment.Top) {
                    AsyncImage(
                        model = b.coverUrl,
                        contentDescription = b.title,
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = b.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = b.author,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    text = b.description ?: "No description available.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))
                Button(onClick = { vm.onToggleShelf() }) {
                    Text("Add / Remove from Shelf")
                }

                Spacer(Modifier.height(24.dp))
                Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 20).dp))
                Text(
                    "Reading",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Divider(Modifier.padding(vertical = 8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { vm.onLogReading(10) }) { Text("+10m") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { vm.onLogReading(30) }) { Text("+30m") }
                }
                Spacer(Modifier.height(8.dp))
                val sessions by vm.sessions.collectAsState()
                val totalMinutes = sessions.sumOf { it.minutes }
                Text(text = "+${totalMinutes}min", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(24.dp))
                Text(
                    "Reviews",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Divider(Modifier.padding(vertical = 8.dp))

                ReviewItem(
                    user = "Alice",
                    content = "A surprisingly engaging read. The pacing around the middle chapters of \"${b.title}\" is excellent.",
                    rating = 4.5f
                )
                ReviewItem(
                    user = "Ben",
                    content = "Loved the world-building and the way the author introduces complex ideas without making it hard to follow.",
                    rating = 4.0f
                )
                ReviewItem(
                    user = "Chloe",
                    content = "Some sections felt slower, but overall it's a book I would happily recommend to friends.",
                    rating = 3.5f
                )

                Spacer(Modifier.height(24.dp))
                Text(
                    "Notes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Divider(Modifier.padding(vertical = 8.dp))
                var noteText by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Add a note") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    if (noteText.isNotBlank()) {
                        vm.addNote(noteText.trim())
                        noteText = ""
                    }
                }) { Text("Save Note") }
                Spacer(Modifier.height(8.dp))
                val notes by vm.notes.collectAsState()
                notes.forEach { n ->
                    NoteItem(
                        content = n.content,
                        onDelete = { vm.removeNote(n.id) }
                    )
                }
                Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 10).dp))
            }
        }
    }
}

@Composable
private fun ReviewItem(
    user: String,
    content: String,
    rating: Float
) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text("$user · ${rating}★", style = MaterialTheme.typography.labelMedium)
        Text(content, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun NoteItem(
    content: String,
    onDelete: () -> Unit
) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(content, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.width(8.dp))
        Button(onClick = onDelete) { Text("Delete") }
    }
}
