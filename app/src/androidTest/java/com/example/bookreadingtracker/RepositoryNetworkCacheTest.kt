package com.example.bookreadingtracker

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bookreadingtracker.data.local.AppDatabase
import com.example.bookreadingtracker.data.remote.BookApi
import com.example.bookreadingtracker.data.repository.DefaultBookRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class RepositoryNetworkCacheTest {
    private lateinit var server: MockWebServer
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
        server.shutdown()
    }

    @Test
    fun searchCachesIntoRoom() = runBlocking {
        val body = """
            { "items": [
              { "id": "id1", "volumeInfo": {"title":"T","authors":["A"],"description":"D","imageLinks":{"thumbnail":"U"} } }
            ] }
        """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))
        val api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
        val repo = DefaultBookRepository(api, db.bookDao(), db.userBookDao(), db.noteDao(), db.readingSessionDao())
        val result = repo.searchBooks("kotlin")
        assertNotNull(result)
        val cached = db.bookDao().getById("id1")
        assertNotNull(cached)
    }
}