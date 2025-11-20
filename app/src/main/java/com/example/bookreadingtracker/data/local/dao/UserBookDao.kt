package com.example.bookreadingtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreadingtracker.data.local.entities.BookEntity
import com.example.bookreadingtracker.data.local.entities.UserBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBookDao {

    @Query(
        """SELECT b.* FROM books b
        INNER JOIN user_books u ON b.id = u.bookId
        WHERE u.status IN ('SHELVED','READING','FINISHED')
        ORDER BY b.title"""
    )
    fun observeShelf(): Flow<List<BookEntity>>

    @Query("SELECT * FROM user_books WHERE bookId = :bookId LIMIT 1")
    suspend fun findForBook(bookId: String): UserBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: UserBookEntity)

    @Query("DELETE FROM user_books WHERE bookId = :bookId")
    suspend fun deleteForBook(bookId: String)

    @Query("SELECT bookId FROM user_books WHERE status IN ('SHELVED','READING','FINISHED')")
    suspend fun getAllBookIds(): List<String>
}
