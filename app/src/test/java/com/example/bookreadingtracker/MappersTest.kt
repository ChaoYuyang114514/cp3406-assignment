package com.example.bookreadingtracker

import com.example.bookreadingtracker.data.remote.GoogleBooksSearchResponse
import com.example.bookreadingtracker.data.remote.toDomain
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {
    @Test
    fun volume_to_domain_maps_fields() {
        val v = GoogleBooksSearchResponse.Volume(
            id = "id1",
            volumeInfo = GoogleBooksSearchResponse.VolumeInfo(
                title = "T",
                authors = listOf("A1", "A2"),
                description = "D",
                imageLinks = GoogleBooksSearchResponse.ImageLinks(thumbnail = "U")
            )
        )
        val b = v.toDomain()
        assertEquals("id1", b.id)
        assertEquals("T", b.title)
        assertEquals("A1, A2", b.author)
        assertEquals("U", b.coverUrl)
        assertEquals("D", b.description)
    }
}