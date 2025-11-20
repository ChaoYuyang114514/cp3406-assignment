package com.example.bookreadingtracker.core

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.room.Room
import com.example.bookreadingtracker.data.local.AppDatabase
import com.example.bookreadingtracker.data.remote.BookApi
import com.example.bookreadingtracker.data.remote.toDomain
import com.example.bookreadingtracker.data.local.entities.BookEntity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShelfRefreshWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "books.db")
            .fallbackToDestructiveMigration()
            .build()
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val api = retrofit.create(BookApi::class.java)

        return try {
            val ids = db.userBookDao().getAllBookIds()
            val now = System.currentTimeMillis()
            ids.forEach { id ->
                val v = api.getBook(id)
                val b = v.toDomain()
                val entity = BookEntity(
                    id = b.id,
                    title = b.title,
                    author = b.author,
                    coverUrl = b.coverUrl,
                    description = b.description,
                    updatedAt = now
                )
                db.bookDao().upsert(entity)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}