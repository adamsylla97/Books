package com.example.books

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.books.db.Book
import com.example.books.db.BooksDB
import com.example.books.db.Category

class EditBookViewModel(private val category: String): ViewModel() {

    val db = BooksDB.booksdb!!

    val removedBooks = MutableLiveData<MutableList<Book>>().apply {
        value = mutableListOf()
    }

    val allBooks = MutableLiveData<List<Book>>().apply {
        value = db.booksDao().getAllSavedBooks()
    }

    val categoryBooks = MutableLiveData<MutableList<Book>>().apply {
        value = getAdventureBooks().toMutableList()
    }

    private val _isHistoryVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        value = false
    }

    val historyButtonText = Transformations.map(_isHistoryVisible) {
        if(_isHistoryVisible.value == true) {
            "Zwiń historię"
        } else {
            "Wczytaj poprzednie"
        }
    }

    val isHistoryVisible: LiveData<Boolean> = _isHistoryVisible

    val historyTextVisible: LiveData<Boolean> = Transformations.map(removedBooks) {
        it.isEmpty()
    }

    fun addToRemovedBooks(book: Book) {
        val prev = removedBooks.value ?: mutableListOf()
        prev.add(book)
        removedBooks.value = prev
    }

    fun removeLastFromRemovedBooks(): Book {
        val prev = removedBooks.value ?: mutableListOf()
        val a = prev.removeAt(prev.size - 1)
        removedBooks.value = prev
        return a
    }

    fun changeVisibility() {
        val current = _isHistoryVisible.value ?: true
        Log.i("supertest123", "Aaa")
        Log.i("supertest123", current.toString())
        _isHistoryVisible.value = !current
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