package com.example.bookreadingtracker.domain.usecase

import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesForBook @Inject constructor(private val repo: BookRepository) {
    operator fun invoke(bookId: String): Flow<List<Note>> = repo.observeNotes(bookId)
}