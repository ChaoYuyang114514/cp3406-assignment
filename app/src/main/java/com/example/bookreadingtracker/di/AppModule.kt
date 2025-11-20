package com.example.bookreadingtracker.di

import android.content.Context
import androidx.room.Room
import com.example.bookreadingtracker.data.local.AppDatabase
import com.example.bookreadingtracker.data.local.dao.BookDao
import com.example.bookreadingtracker.data.local.dao.UserBookDao
import com.example.bookreadingtracker.data.local.dao.NoteDao
import com.example.bookreadingtracker.data.local.dao.ReadingSessionDao
import com.example.bookreadingtracker.data.remote.BookApi
import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.data.repository.DefaultBookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides @Singleton
    fun provideBookApi(retrofit: Retrofit): BookApi =
        retrofit.create(BookApi::class.java)

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "books.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideBookDao(db: AppDatabase): BookDao = db.bookDao()

    @Provides
    fun provideUserBookDao(db: AppDatabase): UserBookDao = db.userBookDao()

    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideReadingSessionDao(db: AppDatabase): ReadingSessionDao = db.readingSessionDao()

    @Provides @Singleton
    fun provideRepository(
        api: BookApi,
        bookDao: BookDao,
        userBookDao: UserBookDao,
        noteDao: NoteDao,
        readingSessionDao: ReadingSessionDao
    ): BookRepository = DefaultBookRepository(api, bookDao, userBookDao, noteDao, readingSessionDao)
}
