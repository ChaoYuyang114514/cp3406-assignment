package com.example.bookreadingtracker

import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.domain.model.Note
import com.example.bookreadingtracker.domain.model.ReadingSession
import com.example.bookreadingtracker.domain.usecase.GetAnalytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsUseCaseTest {
    @Test
    fun analytics_sums_minutes_and_counts_sessions() = runBlocking {
        val repo = object : BookRepository {
            override suspend fun searchBooks(query: String) = emptyList<Book>()
            override fun observeSavedBooks(): Flow<List<Book>> = flowOf(emptyList())
            override suspend fun getByIdFromCache(id: String): Book? = null
            override suspend fun toggleShelf(id: String) {}
            override suspend fun cacheBook(book: Book) {}
            override fun observeNotes(bookId: String): Flow<List<Note>> = flowOf(emptyList())
            override suspend fun upsertNote(note: Note) {}
            override suspend fun deleteNote(id: Long) {}
            override suspend fun logReading(bookId: String, minutes: Int) {}
            override fun sessionsOf(bookId: String): Flow<List<ReadingSession>> = flowOf(emptyList())
            override fun observeAllSessions(): Flow<List<ReadingSession>> = flowOf(
                listOf(
                    ReadingSession(1, "b1", 0L, 10),
                    ReadingSession(2, "b2", 0L, 20)
                )
            )
            override suspend fun refreshBook(id: String) {}
        }
        val useCase = GetAnalytics(repo)
        val res = useCase().first()
        assertEquals(30, res.totalMinutes)
        assertEquals(2, res.sessionsCount)
    }
}