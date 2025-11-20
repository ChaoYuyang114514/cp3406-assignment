package com.example.bookreadingtracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val coverUrl: String?,
    val description: String?,
    val updatedAt: Long
)
