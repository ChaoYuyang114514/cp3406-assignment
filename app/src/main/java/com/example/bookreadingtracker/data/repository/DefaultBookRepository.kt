package com.example.bookreadingtracker.data.repository

import com.example.bookreadingtracker.data.local.dao.BookDao
import com.example.bookreadingtracker.data.local.dao.UserBookDao
import com.example.bookreadingtracker.data.local.dao.NoteDao
import com.example.bookreadingtracker.data.local.dao.ReadingSessionDao
import com.example.bookreadingtracker.data.local.entities.BookEntity
import com.example.bookreadingtracker.data.local.entities.UserBookEntity
import com.example.bookreadingtracker.data.local.entities.NoteEntity
import com.example.bookreadingtracker.data.local.entities.ReadingSessionEntity
import com.example.bookreadingtracker.data.remote.BookApi
import com.example.bookreadingtracker.data.remote.toDomain
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.domain.model.Note
import com.example.bookreadingtracker.domain.model.ReadingSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultBookRepository @Inject constructor(
    private val api: BookApi,
    private val bookDao: BookDao,
    private val userBookDao: UserBookDao,
    private val noteDao: NoteDao,
    private val readingSessionDao: ReadingSessionDao
) : BookRepository {

    override suspend fun searchBooks(query: String): List<Book> {
        val resp = api.searchBooks(query)
        val domain = resp.items.map { it.toDomain() }
        // Cache minimal data locally for detail/shelf usage
        val now = System.currentTimeMillis()
        bookDao.upsertAll(domain.map { it.toEntity(now) })
        return domain
    }

    override fun observeSavedBooks(): Flow<List<Book>> =
        userBookDao.observeShelf().map { list -> list.map { it.toDomain() } }

    override suspend fun getByIdFromCache(id: String): Book? =
        bookDao.getById(id)?.toDomain()

    override suspend fun toggleShelf(id: String) {
        // Simple toggle: if there is a row for this book -> delete, else insert SHELVED
        val existing = userBookDao.findForBook(id)
        if (existing != null) {
            userBookDao.deleteForBook(id)
        } else {
            val entity = UserBookEntity(
                bookId = id,
                status = "SHELVED",
                currentPage = null,
                startedAt = null,
                finishedAt = null
            )
            userBookDao.upsert(entity)
        }
    }

    override suspend fun cacheBook(book: Book) {
        bookDao.upsert(book.toEntity(System.currentTimeMillis()))
    }

    override fun observeNotes(bookId: String): Flow<List<Note>> =
        noteDao.observeByBook(bookId).map { it.map { e -> e.toDomain() } }

    override suspend fun upsertNote(note: Note) {
        val entity = note.toEntity()
        if (entity.id == 0L) noteDao.insert(entity) else noteDao.update(entity)
    }

    override suspend fun deleteNote(id: Long) {
        noteDao.deleteById(id)
    }

    override suspend fun logReading(bookId: String, minutes: Int) {
        val entity = ReadingSessionEntity(
            id = 0,
            bookId = bookId,
            startedAt = System.currentTimeMillis(),
            minutes = minutes
        )
        readingSessionDao.insert(entity)
    }

    override fun sessionsOf(bookId: String): Flow<List<ReadingSession>> =
        readingSessionDao.observeByBook(bookId).map { it.map { e -> e.toDomain() } }

    override fun observeAllSessions(): Flow<List<ReadingSession>> =
        readingSessionDao.observeAll().map { it.map { e -> e.toDomain() } }

    override suspend fun refreshBook(id: String) {
        val v = api.getBook(id)
        val b = v.toDomain()
        bookDao.upsert(b.toEntity(System.currentTimeMillis()))
    }
}

private fun Book.toEntity(updatedAt: Long): BookEntity = BookEntity(
    id = id,
    title = title,
    author = author,
    coverUrl = coverUrl,
    description = description,
    updatedAt = updatedAt
)

private fun BookEntity.toDomain(): Book = Book(
    id = id,
    title = title,
    author = author,
    coverUrl = coverUrl,
    description = description
)

private fun NoteEntity.toDomain(): Note = Note(
    id = id,
    bookId = bookId,
    createdAt = createdAt,
    location = location,
    content = content
)

private fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    bookId = bookId,
    createdAt = createdAt,
    location = location,
    content = content
)

private fun ReadingSessionEntity.toDomain(): ReadingSession = ReadingSession(
    id = id,
    bookId = bookId,
    startedAt = startedAt,
    minutes = minutes
)
