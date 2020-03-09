package com.example.books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.books.db.Book
import kotlinx.android.synthetic.main.item.view.*

class ItemAdapter(val onDeleteClick: (Book)->Unit) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemHolder(inflatedView, onDeleteClick)
    }

    var items = mutableListOf<Book>()

    fun addList(listToAdd: List<Book>) {
        if (items.isNotEmpty())
            items.clear()
        items.addAll(listToAdd)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position], position+1)
    }

    class ItemHolder(val view: View, val onDeleteClick: (Book) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bind(book: Book, position: Int) {
            view.itemTitle.text = book.title
            view.deleteItem.setOnClickListener {
                onDeleteClick(book)
            }
            view.itemNumber.text = "$position."
            if(position == 1) {
                view.label.text = "Obecnie czytana"
                view.label.visibility = View.VISIBLE
            } else if (position == 2) {
                view.label.text = "Kolejna czytana"
                view.label.visibility = View.VISIBLE
            } else {
                view.label.visibility = View.GONE
            }
        }
    }

}