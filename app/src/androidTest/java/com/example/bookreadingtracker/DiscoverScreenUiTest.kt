package com.example.bookreadingtracker

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.domain.model.Note
import com.example.bookreadingtracker.domain.model.ReadingSession
import com.example.bookreadingtracker.domain.usecase.SearchBooks
import com.example.bookreadingtracker.ui.screens.DiscoverScreen
import com.example.bookreadingtracker.ui.screens.DiscoverViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.Ignore
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(maxSdkVersion = 36)
class DiscoverScreenUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @Ignore("Temporarily disabled")
    fun searchShowsResults() {
        val fakeRepo = object : BookRepository {
            override suspend fun searchBooks(query: String): List<Book> = listOf(Book("id1","Title 1","Author","", ""))
            override fun observeSavedBooks(): Flow<List<Book>> = flowOf(emptyList())
            override suspend fun getByIdFromCache(id: String): Book? = null
            override suspend fun toggleShelf(id: String) {}
            override suspend fun cacheBook(book: Book) {}
            override fun observeNotes(bookId: String): Flow<List<Note>> = flowOf(emptyList())
            override suspend fun upsertNote(note: Note) {}
            override suspend fun deleteNote(id: Long) {}
            override suspend fun logReading(bookId: String, minutes: Int) {}
            override fun sessionsOf(bookId: String): Flow<List<ReadingSession>> = flowOf(emptyList())
            override fun observeAllSessions(): Flow<List<ReadingSession>> = flowOf(emptyList())
            override suspend fun refreshBook(id: String) {}
        }
        val vm = DiscoverViewModel(SearchBooks(fakeRepo), com.example.bookreadingtracker.domain.usecase.ToggleShelf(fakeRepo), fakeRepo)
        composeRule.setContent {
            val nav = NavHostController(composeRule.activity)
            DiscoverScreen(nav = nav, vm = vm)
        }
        vm.onQueryChange("kotlin")
        vm.onSearch()
        composeRule.onNodeWithText("Title 1").assertExists()
    }
}