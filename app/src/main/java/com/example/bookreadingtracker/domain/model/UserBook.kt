package com.example.bookreadingtracker.domain.model

data class UserBook(
    val bookId: String,
    val status: Status,
    val currentPage: Int? = null,
    val startedAt: Long? = null,
    val finishedAt: Long? = null
) {
    enum class Status { SHELVED, READING, FINISHED }
}
