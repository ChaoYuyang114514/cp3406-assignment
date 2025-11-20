package com.example.bookreadingtracker.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.ui.nav.Destinations

@Composable
fun ShelfScreen(nav: NavController, vm:ShelfViewModel = hiltViewModel()) {
    val books by vm.books.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 20).dp))
        Text("My Shelf", style=MaterialTheme.typography.headlineSmall)
        if (books.isEmpty()) Text("No books yet.")
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(books) { b ->
                ShelfItem(
                    b = b,
                    onOpen = { nav.navigate(Destinations.detailRoute(b.id)) },
                    onRemove = { vm.onRemoveFromShelf(b.id) }
                )
            }
        }
    }
}

@Composable
private fun ShelfItem(b: Book, onOpen: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = b.coverUrl,
            contentDescription = b.title,
            modifier = Modifier
                .width(64.dp)
                .height(96.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                b.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                b.author,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(8.dp))
        Button(onClick = onOpen) { Text("Open") }
        Spacer(Modifier.width(8.dp))
        Button(onClick = onRemove) { Text("Remove") }
    }
}
