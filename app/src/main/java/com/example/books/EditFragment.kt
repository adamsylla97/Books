package com.example.books


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_edit.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.example.books.databinding.FragmentEditBinding
import com.example.books.db.Book
import com.example.books.db.BooksDB
import kotlinx.android.synthetic.main.fragment_add_book.*
import java.util.*


class EditFragment : Fragment() {

    private val itemAdapter = ItemAdapter{ onDeleteClickButton(it) }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val db = BooksDB.booksdb!!

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_edit, container, false)
//    }

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
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.title = "Edytuj gatunek"

        books.apply {
            layoutManager = LinearLayoutManager(this@EditFragment.context)
            adapter = itemAdapter
        }
        prepareRecyclerView()

        cancelButton.setOnClickListener {
            Toast.makeText(this.context, "Closed", Toast.LENGTH_LONG).show()
        }

        saveButton.setOnClickListener {
            Toast.makeText(this.context, "Saved", Toast.LENGTH_LONG).show()
        }

        addBookButton.setOnClickListener {
            val addBookFragment = AddBookFragment()
            addBookFragment.show(fragmentManager!!, "addbook")
        }

        sharedViewModel.booksFromCategory.observe(this, androidx.lifecycle.Observer {
            itemAdapter.addList(it ?: emptyList())
        })

        searchBook.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateRecyclerView(p0.toString())
            }

        })


    }

    private fun prepareRecyclerView() {
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, dragged: ViewHolder, target: ViewHolder): Boolean {
                val fromPos = dragged.adapterPosition
                val toPos = target.adapterPosition
                val list = itemAdapter.items
                Collections.swap(list, fromPos, toPos)
                itemAdapter.notifyItemMoved(fromPos, toPos)
                itemAdapter.notifyDataSetChanged()
                //w przypadku lagow w przesuwanie itemow bedzie trzeba usunac notify data set changed (prawdopodobnie jest to tym spowodowane)
                //gdy to usuniemy itemy powinny lepiej sie przesuwac jednak tracimy update kolejnosci za kazdym przesunieciem
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            }
        })

        helper.attachToRecyclerView(books)
    }

    private fun updateRecyclerView(value: String) {
        val books = sharedViewModel.booksFromCategory.value ?: emptyList()
        if(value.isNotEmpty()) {
            val a = books.filter {
                it.title.contains(value)
            }
            itemAdapter.addList(a)
        } else {
            itemAdapter.addList(sharedViewModel.originalList.value ?: emptyList())
        }
    }

    private fun onDeleteClickButton(book: Book) {
        val oldBook = db.booksDao().getSpecificBook(book.bookId)
        val types = oldBook.types.toMutableList()
        if(types.contains("adventure")) {
            types.remove("adventure")
        }
        //podobnie jak w poprzednim fragmencie, poprzez new instance bedzie trzeba przekazac gratunek ksiazki jakiej dotyczy aktualny fragment
        val newBook = Book(oldBook.title, types, oldBook.bookId)
        db.booksDao().updateBook(newBook)
        sharedViewModel.updateBooksFromCategory()
    }

}
