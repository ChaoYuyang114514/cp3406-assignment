package com.example.bookreadingtracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookreadingtracker.data.local.dao.BookDao
import com.example.bookreadingtracker.data.local.dao.UserBookDao
import com.example.bookreadingtracker.data.local.dao.NoteDao
import com.example.bookreadingtracker.data.local.dao.ReadingSessionDao
import com.example.bookreadingtracker.data.local.entities.BookEntity
import com.example.bookreadingtracker.data.local.entities.NoteEntity
import com.example.bookreadingtracker.data.local.entities.ReadingSessionEntity
import com.example.bookreadingtracker.data.local.entities.UserBookEntity

@Database(
    entities = [BookEntity::class, UserBookEntity::class, ReadingSessionEntity::class, NoteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun userBookDao(): UserBookDao
    abstract fun noteDao(): NoteDao
    abstract fun readingSessionDao(): ReadingSessionDao
}
