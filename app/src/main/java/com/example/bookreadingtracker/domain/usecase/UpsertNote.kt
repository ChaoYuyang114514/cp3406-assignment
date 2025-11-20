package com.example.bookreadingtracker.domain.usecase

import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.Note
import javax.inject.Inject

class UpsertNote @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(note: Note) = repo.upsertNote(note)
}
