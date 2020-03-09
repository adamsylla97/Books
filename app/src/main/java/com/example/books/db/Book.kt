package com.example.books.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "types") val types: List<String>,
    @PrimaryKey(autoGenerate = true) val bookId: Int = 0
)