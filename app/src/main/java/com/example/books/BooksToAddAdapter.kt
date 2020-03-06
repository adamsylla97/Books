package com.example.books

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_book_item.view.*

class BooksToAddAdapter : RecyclerView.Adapter<BooksToAddAdapter.BooksHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.add_book_item, parent, false)
        return BooksHolder(inflatedView)
    }

    val books = mutableListOf<String>()

    fun addList(listToAdd: List<String>) {
        if (books.isNotEmpty())
            books.clear()
        books.addAll(listToAdd)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BooksHolder, position: Int) {
        holder.bind(books[position])
    }

    class BooksHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(title: String) {
            view.bookTitle.text = title
            Log.i("supertest123", title)
        }
    }

}