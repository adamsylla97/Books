package com.example.books


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_edit.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class EditFragment : Fragment() {

    private val itemAdapter = ItemAdapter()
    private val myDataSet = listOf("1 Book1", "2 Book2", "3 Book3")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.title = "Edytuj gatunek"

        books.apply {
            layoutManager = LinearLayoutManager(this@EditFragment.context)
            adapter = itemAdapter
        }
        prepareRecyclerView()

        addBookButton.setOnClickListener {
            val addBookFragment = AddBookFragment()
            addBookFragment.show(fragmentManager, "addbook")
        }

        itemAdapter.addList(myDataSet)

    }

    private fun prepareRecyclerView() {
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(recyclerView: RecyclerView, dragged: ViewHolder, target: ViewHolder): Boolean {
                val fromPos = dragged.adapterPosition
                val toPos = target.adapterPosition
                Collections.swap(myDataSet, fromPos, toPos)
                itemAdapter.notifyItemMoved(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                // remove from adapter
            }
        })

        helper.attachToRecyclerView(books)
    }

}
