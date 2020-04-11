package com.example.books


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.books.databinding.FragmentEditBinding
import com.example.books.db.Book
import com.example.books.db.BooksDB
import kotlinx.android.synthetic.main.fragment_edit.*
import java.util.*
import kotlin.Exception


class EditFragment : Fragment() {

    private val category = "adventure"

    private val itemAdapter = ItemAdapter{ onDeleteClickButton(it) }
    private val addBookAdapter = BooksToAddAdapter { flag, book ->
        onAddClickButton(flag, book)
    }
    private val historyAdapter: HistoryAdapter = HistoryAdapter()
    private val viewModelEditBook = EditBookViewModel(category)
    private val db = BooksDB.booksdb!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentEditBinding>(
            inflater,
            R.layout.fragment_edit,
            container,
            false
        ).apply {
            lifecycleOwner = this@EditFragment
            viewModel = viewModelEditBook
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.title = "Edytuj gatunek"

        books.apply {
            layoutManager = LinearLayoutManager(this@EditFragment.context)
            adapter = itemAdapter
        }
        booksToAdd.apply {
            layoutManager = LinearLayoutManager(this@EditFragment.context)
            adapter = addBookAdapter
        }
        history.apply {
            layoutManager = LinearLayoutManager(this@EditFragment.context)
            adapter = historyAdapter
        }
        prepareRecyclerView()

        cancelButton.setOnClickListener {
            Toast.makeText(this.context, "Closed", Toast.LENGTH_LONG).show()
        }

        saveButton.setOnClickListener {
            Toast.makeText(this.context, "Saved", Toast.LENGTH_LONG).show()
        }

        previousBookButton.setOnClickListener {
            onPreviousButtonClicked()
        }

        nextBookButton.setOnClickListener {
            onNextButtonClicked()
        }

        viewModelEditBook.isHistoryVisible.observe(this, Observer{
            if(it) {
                historyButton.background = resources.getDrawable(R.drawable.yellow_bordered_button)
            } else {
                historyButton.background = resources.getDrawable(R.drawable.default_bordered_button)
            }
        })

        var list = listOf<Book>()

        viewModelEditBook.categoryBooks.observe(this, androidx.lifecycle.Observer {
            itemAdapter.addList(it ?: emptyList())
            try{
                obecnieCzytana.text = "Obecnie czytana: " + it[0].title
            } catch (e: Exception) {
                obecnieCzytana.text = "Obecnie czytana:"
            }

            try {
                nastepnieCzytana.text = "Nastepna czytana: " + it[1].title
            } catch (e: Exception) {
                nastepnieCzytana.text = "Następna czytana:"
            }
            list = it
        })

        viewModelEditBook.allBooks.observe(this, androidx.lifecycle.Observer {
            addBookAdapter.addList(it ?: emptyList())
        })

        viewModelEditBook.removedBooks.observe(this, Observer {
            historyAdapter.addList(it)
        })

        searchBook.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onSearch(p0.toString())
                if(p0.toString().isNotEmpty()){
                    obecnieCzytana.visibility = View.INVISIBLE
                    nastepnieCzytana.visibility = View.INVISIBLE
                    booksToAdd.visibility = View.VISIBLE
                    books.visibility = View.GONE
                    nastepnaLabel.visibility = View.VISIBLE
                } else {
                    obecnieCzytana.visibility = View.VISIBLE
                    nastepnieCzytana.visibility = View.VISIBLE
                    try{
                        obecnieCzytana.text = "Obecnie czytana: " + itemAdapter.items[0].title
                    } catch (e: Exception) {
                        obecnieCzytana.text = "Obecnie czytana:"
                    }
                    try{
                        nastepnieCzytana.text = "Nastepnie czytana: " + itemAdapter.items[1].title
                    } catch (e: Exception) {
                        nastepnieCzytana.text = "Nastepnie czytana:"
                    }
                    nastepnaLabel.visibility = View.GONE
                    booksToAdd.visibility = View.GONE
                    books.visibility = View.VISIBLE
                }
            }

        })


    }

    private fun onPreviousButtonClicked() {
        try{
            if(viewModelEditBook.removedBooks.value?.size ?: 0 == 0) {
                displayInfo("Brak przewiniętych książek.")
            } else {
                val previousBook = viewModelEditBook.removeLastFromRemovedBooks()
                viewModelEditBook.categoryBooks.value?.add(0,previousBook)
                Log.i("supertest123", viewModelEditBook.categoryBooks.value.toString())
                itemAdapter.addList(viewModelEditBook.categoryBooks.value ?: emptyList())
                try{
                    obecnieCzytana.text = "Obecnie czytana: " + itemAdapter.items[0].title
                } catch (e: Exception) {
                    obecnieCzytana.text = "Obecnie czytana:"
                }
                try{
                    nastepnieCzytana.text = "Nastepnie czytana: " + itemAdapter.items[1].title
                } catch (e: Exception) {
                    nastepnieCzytana.text = "Nastepnie czytana:"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun onNextButtonClicked() {
        try{
            if(viewModelEditBook.categoryBooks.value?.isEmpty() ?: true) {
                displayInfo("Brak ksiazek w kolejce.")
            } else {
                val a: Book? = viewModelEditBook.categoryBooks.value?.removeAt(0)
                Log.i("supertest123", viewModelEditBook.categoryBooks.value.toString())
                itemAdapter.addList(viewModelEditBook.categoryBooks.value ?: emptyList())
                viewModelEditBook.addToRemovedBooks(a!!)
                Log.i("supertest123", "aaa")
                Log.i("supertest123", viewModelEditBook.removedBooks.value.toString())
                try{
                    obecnieCzytana.text = "Obecnie czytana: " + itemAdapter.items[0].title
                } catch (e: Exception) {
                    obecnieCzytana.text = "Obecnie czytana:"
                }
                try{
                    nastepnieCzytana.text = "Nastepnie czytana: " + itemAdapter.items[1].title
                } catch (e: Exception) {
                    nastepnieCzytana.text = "Nastepnie czytana:"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun displayInfo(value: String) {
        Toast.makeText(this.context, value, Toast.LENGTH_LONG).show()
    }

    private fun onDeleteClickButton(book: Book) {
        val categoryBooks: MutableList<Book> = viewModelEditBook.categoryBooks.value?.toMutableList() ?: mutableListOf()
        categoryBooks.remove(book)
        viewModelEditBook.categoryBooks.value = categoryBooks

    }

    private fun onAddClickButton(addAsNext: Boolean, book: Book) {
        if(addAsNext) {
            val currentCategoryBooks: MutableList<Book> = viewModelEditBook.categoryBooks.value?.toMutableList() ?: mutableListOf()
            try {
                currentCategoryBooks.add(1, book)
            } catch (e: Exception) {
                currentCategoryBooks.add(book)
            }
            viewModelEditBook.categoryBooks.value = currentCategoryBooks
        } else {
            val currentCategoryBooks: MutableList<Book> = viewModelEditBook.categoryBooks.value?.toMutableList() ?: mutableListOf()
            currentCategoryBooks.add(book)
            viewModelEditBook.categoryBooks.value = currentCategoryBooks
        }

        searchBook.text.clear()
    }

    private fun onSearch(value: String) {
        val books = viewModelEditBook.allBooks.value ?: emptyList()
        if(value.isNotEmpty()) {
            val a = books.filter {
                it.title.contains(value.toLowerCase())
            }
            addBookAdapter.addList(a)
        } else {
            addBookAdapter.addList(books)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModelEditBook.updateDataBase()
    }

    //metoda odpowiedzialna za przenoszenie itemow w recycler view
    private fun prepareRecyclerView() {
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, dragged: ViewHolder, target: ViewHolder): Boolean {
                val fromPos = dragged.adapterPosition
                val toPos = target.adapterPosition
                val list = itemAdapter.items
                Collections.swap(list, fromPos, toPos)
                itemAdapter.notifyItemMoved(fromPos, toPos)
                obecnieCzytana.text = "Obecnie czytana: " + list[0]?.title
                nastepnieCzytana.text = "Nastepna czytana: " + list[1].title
                //w przypadku lagow w przesuwanie itemow bedzie trzeba usunac notify data set changed (prawdopodobnie jest to tym spowodowane)
                //gdy to usuniemy itemy powinny lepiej sie przesuwac jednak tracimy update kolejnosci za kazdym przesunieciem
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {

            }
        })

        helper.attachToRecyclerView(books)
    }

}
