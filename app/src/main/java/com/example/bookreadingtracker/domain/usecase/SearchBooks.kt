package com.example.bookreadingtracker.domain.usecase

import com.example.bookreadingtracker.data.repository.BookRepository
import javax.inject.Inject

class SearchBooks @Inject constructor(private val repo: BookRepository) {
    suspend operator fun invoke(query: String) = repo.searchBooks(query)
}
