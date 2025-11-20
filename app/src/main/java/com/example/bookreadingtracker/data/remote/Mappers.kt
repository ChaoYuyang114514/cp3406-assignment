package com.example.bookreadingtracker.data.remote

import com.example.bookreadingtracker.domain.model.Book

fun GoogleBooksSearchResponse.Volume.toDomain(): Book = Book(
    id = id,
    title = volumeInfo.title ?: "Untitled",
    author = volumeInfo.authors?.joinToString() ?: "Unknown",
    coverUrl = volumeInfo.imageLinks?.thumbnail,
    description = volumeInfo.description
)
