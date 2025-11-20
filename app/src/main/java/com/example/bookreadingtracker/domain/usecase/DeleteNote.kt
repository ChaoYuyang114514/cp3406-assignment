package com.example.bookreadingtracker.domain.usecase

import com.example.bookreadingtracker.data.repository.BookRepository
import javax.inject.Inject

class DeleteNote @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(id: Long) = repo.deleteNote(id)
}