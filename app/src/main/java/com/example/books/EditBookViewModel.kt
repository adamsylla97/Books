package com.example.books

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.books.db.Book
import com.example.books.db.BooksDB

class EditBookViewModel(private val category: String): ViewModel() {

    val db = BooksDB.booksdb!!

    val allBooks = MutableLiveData<List<Book>>().apply {
        value = db.booksDao().getAllSavedBooks()
    }

    val categoryBooks = MutableLiveData<MutableList<Book>>().apply {
        value = getAdventureBooks().toMutableList()
    }

    private fun getAdventureBooks(): List<Book> {
        val books = db.booksDao().getAllSavedBooks()
        val wantedBooks = books.filter { it.types.contains(category) }
        return wantedBooks
    }

    fun updateBooks() {
        categoryBooks.value = getAdventureBooks().toMutableList()
        allBooks.value = db.booksDao().getAllSavedBooks()
    }

    private fun updateDataBase() {
        val dataBaseBooks = allBooks.value?.toMutableList() ?: mutableListOf()
        val categoryBooks = categoryBooks.value ?: mutableListOf()
        dataBaseBooks.forEach { dbBook ->
            var doesContain: Boolean = false
            categoryBooks.forEach { categoryBook ->
                if(categoryBook.title == dbBook.title) {
                    doesContain = true
                }
            }
            if(doesContain) {
                if(!dbBook.types.contains(category)) {
                    val types = dbBook.types.toMutableList()
                    types.add(category)
                    db.booksDao().updateBook(Book(dbBook.title, types, dbBook.bookId))
                }
            } else {
                if(dbBook.types.contains(category)) {
                    val types = dbBook.types.toMutableList()
                    types.remove(category)
                    db.booksDao().updateBook(Book(dbBook.title, types, dbBook.bookId))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        updateDataBase()
    }

}