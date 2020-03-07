package com.example.books

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.books.db.Book
import com.example.books.db.BooksDB

class SharedViewModel: ViewModel() {

    val db = BooksDB.booksdb!!
    //books from category and get adventure books - these two should be copied for every category
    val booksFromCategory = MutableLiveData<List<Book>>().apply {
        value = getAdventureBooks()
    }
    val searchEditFragment = MutableLiveData<String>()

    private fun getAdventureBooks(): List<Book> {
        val books = db.booksDao().getAllSavedBooks()
        val wantedBooks = books.filter { it.types.contains("adventure") }
        return wantedBooks
    }

    fun updateBooksFromCategory() {
        booksFromCategory.value = getAdventureBooks()
    }

}