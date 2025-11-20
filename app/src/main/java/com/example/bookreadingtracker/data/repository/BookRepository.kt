package com.example.bookreadingtracker.data.repository

import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.domain.model.Note
import com.example.bookreadingtracker.domain.model.ReadingSession
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBooks(query: String): List<Book>
    fun observeSavedBooks(): Flow<List<Book>>
    suspend fun getByIdFromCache(id: String): Book?
    suspend fun toggleShelf(id: String)
    suspend fun cacheBook(book: Book)
    fun observeNotes(bookId: String): Flow<List<Note>>
    suspend fun upsertNote(note: Note)
    suspend fun deleteNote(id: Long)
    suspend fun logReading(bookId: String, minutes: Int)
    fun sessionsOf(bookId: String): Flow<List<ReadingSession>>
    fun observeAllSessions(): Flow<List<ReadingSession>>
    suspend fun refreshBook(id: String)
}
