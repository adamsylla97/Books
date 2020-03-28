package com.example.books

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.books.db.Book
import com.example.books.db.BooksDB
import com.example.books.db.Category

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
        val cat: Category? = db.categoryDao().getSpecificCategory(category)
        return cat?.categoryBooks ?: wantedBooks
    }

    fun updateDataBase() {
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

        val cat: Category? = db.categoryDao().getSpecificCategory(category)
        if(cat == null) {
            val newCategory = Category(category, categoryBooks.toList())
            db.categoryDao().insertCategory(newCategory)
        } else {
            val updatedCategory = Category(cat.categoryName, categoryBooks.toList(), cat.categoryId)
            db.categoryDao().updateCategory(updatedCategory)
        }

    }

    override fun onCleared() {
        super.onCleared()
        updateDataBase()
    }

}