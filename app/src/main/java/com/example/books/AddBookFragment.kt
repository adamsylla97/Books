package com.example.books

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        closeButton.setOnClickListener {
            dismiss()
        }

        booksAdapter.addList(sharedViewModel.allBooksFromDB.value ?: emptyList())

        search.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateRecyclerView(p0.toString())
            }

        })

    }

    private fun updateRecyclerView(value: String) {
        val books = sharedViewModel.searchAllBooks.value ?: emptyList()
        if(value.isNotEmpty()) {
            val a = books.filter {
                it.title.contains(value)
            }
            booksAdapter.addList(a)
        } else {
            booksAdapter.addList(sharedViewModel.allBooksFromDB.value ?: emptyList())
        }
    }

    private fun onAddBookButtonClick(book: Book) {
        val oldBook = db.booksDao().getSpecificBook(book.bookId)
        val types = oldBook.types.toMutableList()
        //adventure to tutaj typ ksiazki, w zaleznosci od implementacji jesli tych gratunkow bedzie wiecej bedzie trzeba dostosowac implementacje
        //proponuje przekazywaćdo fragmentu w jakiej aktualnie jesteśmy kategorii (tworzenie fragmentu poprze new instance)
        if(!types.contains("adventure")) {
            types.add("adventure")
        }
        val newBook = Book(oldBook.title, types)
        db.booksDao().insertBook(newBook)
        sharedViewModel.updateBooksFromCategory()
    }


}
