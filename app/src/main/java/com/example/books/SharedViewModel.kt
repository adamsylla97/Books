package com.example.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.books.db.Book
import com.example.books.db.BooksDB

class SharedViewModel: ViewModel() {

    val db = BooksDB.booksdb!!

    val booksFromCategory = MutableLiveData<List<Book>>().apply {
        value = getAdventureBooks()
    }
    val originalList = MutableLiveData<List<Book>>().apply {
        value = getAdventureBooks()
    }
    val searchEditFragment = MutableLiveData<String>()

    val allBooksFromDB = MutableLiveData<List<Book>>().apply {
        value = db.booksDao().getAllSavedBooks()
    }

    val searchAllBooks = MutableLiveData<List<Book>>().apply {
        value = db.booksDao().getAllSavedBooks()
    }

    private fun getAdventureBooks(): List<Book> {
        val books = db.booksDao().getAllSavedBooks()
        val wantedBooks = books.filter { it.types.contains("adventure") }
        return wantedBooks
    }

    fun updateBooksFromCategory() {
        booksFromCategory.value = getAdventureBooks()
        originalList.value = getAdventureBooks()
    }

    //shared view model to klasa do ktorej maja dostep zarowno edit fragment oraz add book fragment.
    //moga odczytywac z niej dane za kazdym razem gdy cos jest zmienione w tej klasie (np dodanie czegos do listy) spowoduje to zmiany wszedzie, gdzie ta klasa jest
    //wykorzystana

}