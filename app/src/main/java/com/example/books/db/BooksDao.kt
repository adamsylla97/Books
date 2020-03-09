package com.example.books.db

import androidx.room.*

@Dao
interface BooksDao {

    @Query("SELECT * FROM book")
    fun getAllSavedBooks(): List<Book>

    @Insert
    fun insertBook(vararg books: Book)

    @Delete
    fun deleteBook(book: Book)

    @Update
    fun updateBook(book: Book)

    @Query("SELECT * FROM book WHERE bookId = :givenId")
    fun getSpecificBook(givenId: Int): Book

    @Query("DELETE FROM book WHERE bookId > 0")
    fun clearDatabase()

}