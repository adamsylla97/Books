package com.example.books

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.books.db.Book
import com.example.books.db.BooksDB
import kotlinx.android.synthetic.main.fragment_add_book.*

class AddBookFragment : DialogFragment() {

    private val db = BooksDB.booksdb!!
    private val booksAdapter = BooksToAddAdapter{onAddBookButtonClick(it)}
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        booksToAdd.apply {
            layoutManager = LinearLayoutManager(this@AddBookFragment.context)
            adapter = booksAdapter
        }

        val books = db.booksDao().getAllSavedBooks()
        closeButton.setOnClickListener {
            dismiss()
        }

        booksAdapter.addList(books)

    }

    private fun onAddBookButtonClick(book: Book) {
        val oldBook = db.booksDao().getSpecificBook(book.bookId)
        val types = oldBook.types.toMutableList()
        if(!types.contains("adventure")) {
            types.add("adventure")
        }
        val newBook = Book(oldBook.title, types, oldBook.bookId)
        db.booksDao().updateBook(newBook)
        sharedViewModel.updateBooksFromCategory()
    }


}
