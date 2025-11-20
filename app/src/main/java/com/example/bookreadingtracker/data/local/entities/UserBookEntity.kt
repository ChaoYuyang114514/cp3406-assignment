package com.example.bookreadingtracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_books")
data class UserBookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val status: String, // SHELVED | READING | FINISHED
    val currentPage: Int?,
    val startedAt: Long?,
    val finishedAt: Long?
)
