package com.example.bookreadingtracker.domain.usecase

import com.example.bookreadingtracker.data.repository.BookRepository
import javax.inject.Inject

class LogReading @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(bookId: String, minutes: Int) = repo.logReading(bookId, minutes)
}
