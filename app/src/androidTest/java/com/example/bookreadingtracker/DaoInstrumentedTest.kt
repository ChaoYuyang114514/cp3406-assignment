package com.example.bookreadingtracker

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.Room
import com.example.bookreadingtracker.data.local.AppDatabase
import com.example.bookreadingtracker.data.local.entities.BookEntity
import com.example.bookreadingtracker.data.local.entities.NoteEntity
import com.example.bookreadingtracker.data.local.entities.UserBookEntity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaoInstrumentedTest {
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() { db.close() }

    @Test
    fun shelfJoinReturnsBook() = runBlocking {
        val now = System.currentTimeMillis()
        db.bookDao().upsert(BookEntity("b1", "T", "A", null, null, now))
        db.userBookDao().upsert(UserBookEntity(bookId = "b1", status = "SHELVED", currentPage = null, startedAt = null, finishedAt = null))
        val got = db.userBookDao().observeShelf().first()
        assertNotNull(got)
        assertEquals(1, got.size)
        assertEquals("b1", got[0].id)
    }

    @Test
    fun notesInsertAndDelete() = runBlocking {
        val now = System.currentTimeMillis()
        val note = NoteEntity(id = 0, bookId = "b2", createdAt = now, location = null, content = "c")
        db.noteDao().insert(note)
        val notes = db.noteDao().observeByBook("b2").first()
        assertEquals(1, notes.size)
    }
}