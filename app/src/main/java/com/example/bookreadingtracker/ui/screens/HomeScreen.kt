package com.example.bookreadingtracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.ui.nav.Destinations

@Composable
fun HomeScreen(nav: NavController, vm: ShelfViewModel = hiltViewModel()) {
    val books by vm.books.collectAsState()
    val columns = if (books.isNotEmpty() && books.size % 3 == 0) 3 else 2
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(Modifier.height((LocalConfiguration.current.screenHeightDp / 20).dp))
        Text("Welcome!", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text("Use Discover to search books", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(books) { b -> HomeGridItem(b) { nav.navigate(Destinations.detailRoute(b.id)) } }
        }
    }
}

@Composable
private fun HomeGridItem(b: Book, onClick: () -> Unit) {
    AsyncImage(
        model = b.coverUrl,
        contentDescription = b.title,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f/3f)
            .clickable(onClick = onClick)
    )
}
