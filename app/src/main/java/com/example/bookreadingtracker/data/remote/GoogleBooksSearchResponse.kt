package com.example.bookreadingtracker.data.remote

data class GoogleBooksSearchResponse(
    val items: List<Volume> = emptyList()
) {
    data class Volume(
        val id: String,
        val volumeInfo: VolumeInfo
    )
    data class VolumeInfo(
        val title: String?,
        val authors: List<String>?,
        val description: String?,
        val imageLinks: ImageLinks?
    )
    data class ImageLinks(
        val thumbnail: String?
    )
}
