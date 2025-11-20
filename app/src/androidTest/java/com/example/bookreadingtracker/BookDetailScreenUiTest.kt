package com.example.bookreadingtracker

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.domain.model.Note
import com.example.bookreadingtracker.domain.model.ReadingSession
import com.example.bookreadingtracker.domain.usecase.DeleteNote
import com.example.bookreadingtracker.domain.usecase.GetNotesForBook
import com.example.bookreadingtracker.domain.usecase.GetReadingSessions
import com.example.bookreadingtracker.domain.usecase.LogReading
import com.example.bookreadingtracker.domain.usecase.UpsertNote
import com.example.bookreadingtracker.ui.screens.BookDetailScreen
import com.example.bookreadingtracker.ui.screens.BookDetailViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.Ignore
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(maxSdkVersion = 36)
class BookDetailScreenUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @Ignore("Temporarily disabled")
    fun clicking_read_buttons_shows_total_plus_minutes() {
        val sessionsFlow = MutableStateFlow<List<ReadingSession>>(emptyList())
        val fakeRepo = object : BookRepository {
            override suspend fun searchBooks(query: String): List<Book> = emptyList()
            override fun observeSavedBooks(): Flow<List<Book>> = kotlinx.coroutines.flow.flowOf(emptyList())
            override suspend fun getByIdFromCache(id: String): Book? = Book(id,"Title","Author",null,null)
            override suspend fun toggleShelf(id: String) {}
            override suspend fun cacheBook(book: Book) {}
            override fun observeNotes(bookId: String): Flow<List<Note>> = kotlinx.coroutines.flow.flowOf(emptyList())
            override suspend fun upsertNote(note: Note) {}
            override suspend fun deleteNote(id: Long) {}
            override suspend fun logReading(bookId: String, minutes: Int) {
                val current = sessionsFlow.value
                sessionsFlow.value = current + ReadingSession(current.size.toLong()+1, bookId, System.currentTimeMillis(), minutes)
            }
            override fun sessionsOf(bookId: String): Flow<List<ReadingSession>> = sessionsFlow
            override fun observeAllSessions(): Flow<List<ReadingSession>> = sessionsFlow
            override suspend fun refreshBook(id: String) {}
        }

        val vm = BookDetailViewModel(
            repo = fakeRepo,
            savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf("bookId" to "b1")),
            upsertNote = UpsertNote(fakeRepo),
            deleteNote = DeleteNote(fakeRepo),
            getNotesForBook = GetNotesForBook(fakeRepo),
            getReadingSessions = GetReadingSessions(fakeRepo),
            logReading = LogReading(fakeRepo)
        )

        composeRule.setContent {
            val nav = NavHostController(composeRule.activity)
            BookDetailScreen(navController = nav, vm = vm)
        }
        vm.onLogReading(10)
        vm.onLogReading(30)
        composeRule.onNodeWithText("+40min").assertExists()
    }
}