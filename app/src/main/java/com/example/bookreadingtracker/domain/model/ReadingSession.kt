package com.example.bookreadingtracker.domain.model

data class ReadingSession(
    val id: Long,
    val bookId: String,
    val startedAt: Long,
    val minutes: Int
)
