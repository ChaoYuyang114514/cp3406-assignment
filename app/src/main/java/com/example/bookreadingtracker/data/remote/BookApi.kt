package com.example.bookreadingtracker.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): GoogleBooksSearchResponse

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") id: String): GoogleBooksSearchResponse.Volume
}
