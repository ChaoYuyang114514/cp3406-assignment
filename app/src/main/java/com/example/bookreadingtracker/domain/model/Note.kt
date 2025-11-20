package com.example.bookreadingtracker.domain.model

data class Note(
    val id: Long,
    val bookId: String,
    val createdAt: Long,
    val location: String?,
    val content: String
)
