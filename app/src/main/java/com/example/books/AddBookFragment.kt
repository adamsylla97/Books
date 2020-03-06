package com.example.books

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_add_book.*

class AddBookFragment : DialogFragment() {

    private val booksAdapter = BooksToAddAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        booksToAdd.apply {
            layoutManager = LinearLayoutManager(this@AddBookFragment.context)
            adapter = booksAdapter
        }

        val books = listOf("Book 1", "Book 2", "Book 3")

        booksAdapter.addList(books)

    }


}
