package com.example.bookreadingtracker

import app.cash.turbine.test
import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.domain.model.Note
import com.example.bookreadingtracker.domain.model.ReadingSession
import com.example.bookreadingtracker.domain.usecase.GetAnalytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAnalyticsTurbineTest {
    @Test
    fun analytics_emits_total_and_sorted_perBook() = runTest {
        val repo = object : BookRepository {
            override suspend fun searchBooks(query: String): List<Book> = emptyList()
            override fun observeSavedBooks(): Flow<List<Book>> = flowOf(emptyList())
            override suspend fun getByIdFromCache(id: String): Book? = when (id) {
                "b1" -> Book("b1","B1","A1",null,null)
                "b2" -> Book("b2","B2","A2",null,null)
                else -> null
            }
            override suspend fun toggleShelf(id: String) {}
            override suspend fun cacheBook(book: Book) {}
            override fun observeNotes(bookId: String): Flow<List<Note>> = flowOf(emptyList())
            override suspend fun upsertNote(note: Note) {}
            override suspend fun deleteNote(id: Long) {}
            override suspend fun logReading(bookId: String, minutes: Int) {}
            override fun sessionsOf(bookId: String): Flow<List<ReadingSession>> = flowOf(emptyList())
            override fun observeAllSessions(): Flow<List<ReadingSession>> = flowOf(
                listOf(
                    ReadingSession(1,"b1",0L,10),
                    ReadingSession(2,"b2",0L,20),
                    ReadingSession(3,"b1",0L,5)
                )
            )
            override suspend fun refreshBook(id: String) {}
        }

        val useCase = GetAnalytics(repo)
        useCase().test {
            val item = awaitItem()
            assertEquals(35, item.totalMinutes)
            assertEquals(listOf("b2","b1"), item.perBook.map { it.bookId })
            cancelAndIgnoreRemainingEvents()
        }
    }
}