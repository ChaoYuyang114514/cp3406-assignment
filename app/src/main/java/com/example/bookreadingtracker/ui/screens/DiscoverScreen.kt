package com.example.bookreadingtracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.ui.nav.Destinations

@Composable
fun DiscoverScreen(
    nav: NavController,
    vm: DiscoverViewModel = hiltViewModel()
) {
    val s = vm.uiState
    val snackbarHostState = SnackbarHostState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 20).dp))
        SnackbarHost(hostState = snackbarHostState)

        OutlinedTextField(
            value = s.query,
            onValueChange = vm::onQueryChange,
            label = { Text("Search books") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = vm::onSearch,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }
        Spacer(Modifier.height(16.dp))

        when {
            s.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            s.errorMessage != null -> {
                LaunchedEffect(s.errorMessage) {
                    s.errorMessage?.let { snackbarHostState.showSnackbar(it) }
                }
            }

            else -> BookList(
                list = s.results,
                onClick = { nav.navigate(Destinations.detailRoute(it.id)) },
                onAdd = { vm.onAddToShelf(it) }
            )
        }
    }
}

@Composable
fun BookList(list: List<Book>, onClick: (Book) -> Unit, onAdd: (Book) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(list) { BookItem(it, onClick = { onClick(it) }, onAdd = { onAdd(it) }) }
    }
}

@Composable
fun BookItem(b: Book, onClick: () -> Unit, onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AsyncImage(
            model = b.coverUrl,
            contentDescription = b.title,
            modifier = Modifier
                .width(64.dp)
                .height(96.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f).clickable(onClick = onClick)) {
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
        Button(onClick = onAdd) { Text("Add") }
    }
}
