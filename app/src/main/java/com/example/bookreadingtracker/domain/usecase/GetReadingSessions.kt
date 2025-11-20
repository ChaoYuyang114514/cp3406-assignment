package com.example.bookreadingtracker.domain.usecase

import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.ReadingSession
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReadingSessions @Inject constructor(private val repo: BookRepository) {
    operator fun invoke(bookId: String): Flow<List<ReadingSession>> = repo.sessionsOf(bookId)
}