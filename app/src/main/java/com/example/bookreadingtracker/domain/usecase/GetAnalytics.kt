package com.example.bookreadingtracker.domain.usecase

import com.example.bookreadingtracker.data.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class Analytics(
    val totalMinutes: Int,
    val sessionsCount: Int,
    val perBook: List<BookStat>
)

data class BookStat(
    val bookId: String,
    val title: String,
    val coverUrl: String?,
    val minutes: Int
)

class GetAnalytics @Inject constructor(private val repo: BookRepository) {
    operator fun invoke(): Flow<Analytics> =
        repo.observeAllSessions().map { list ->
            val total = list.sumOf { it.minutes }
            val byBook = list.groupBy { it.bookId }
                .map { (bookId, sessions) ->
                    val minutes = sessions.sumOf { it.minutes }
                    val book = repo.getByIdFromCache(bookId)
                    BookStat(
                        bookId = bookId,
                        title = book?.title ?: "Unknown",
                        coverUrl = book?.coverUrl,
                        minutes = minutes
                    )
                }
                .sortedByDescending { it.minutes }
            Analytics(totalMinutes = total, sessionsCount = list.size, perBook = byBook)
        }
}
