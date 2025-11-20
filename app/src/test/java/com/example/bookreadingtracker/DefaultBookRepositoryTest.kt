package com.example.bookreadingtracker

import com.example.bookreadingtracker.data.local.dao.BookDao
import com.example.bookreadingtracker.data.local.dao.UserBookDao
import com.example.bookreadingtracker.data.local.dao.NoteDao
import com.example.bookreadingtracker.data.local.dao.ReadingSessionDao
import com.example.bookreadingtracker.data.remote.BookApi
import com.example.bookreadingtracker.data.remote.GoogleBooksSearchResponse
import com.example.bookreadingtracker.data.repository.DefaultBookRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultBookRepositoryTest {
    @Test
    fun searchBooks_caches_to_BookDao_and_returns_domain() = runBlocking {
        val api = mockk<BookApi>()
        val bookDao = mockk<BookDao>(relaxed = true)
        val userBookDao = mockk<UserBookDao>()
        val noteDao = mockk<NoteDao>()
        val readingSessionDao = mockk<ReadingSessionDao>()

        val resp = GoogleBooksSearchResponse(
            items = listOf(
                GoogleBooksSearchResponse.Volume(
                    id = "id1",
                    volumeInfo = GoogleBooksSearchResponse.VolumeInfo(
                        title = "T",
                        authors = listOf("A"),
                        description = "D",
                        imageLinks = GoogleBooksSearchResponse.ImageLinks(thumbnail = "U")
                    )
                )
            )
        )
        coEvery { api.searchBooks(any()) } returns resp

        val repo = DefaultBookRepository(api, bookDao, userBookDao, noteDao, readingSessionDao)
        val result = repo.searchBooks("kotlin")

        assertEquals(1, result.size)
        assertEquals("id1", result[0].id)
        assertEquals("T", result[0].title)

        coVerify(exactly = 1) { bookDao.upsertAll(any()) }
    }
}