package com.example.books.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @ColumnInfo(name = "categoryName") val categoryName: String,
    @ColumnInfo(name = "categoryBooks") val categoryBooks: List<Book>,
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0
)