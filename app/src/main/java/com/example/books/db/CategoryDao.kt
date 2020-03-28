package com.example.books.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryDao {

    @Insert
    fun insertCategory(vararg category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category WHERE categoryName == :categoryName")
    fun getSpecificCategory(categoryName: String): Category

}