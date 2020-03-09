package com.example.books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.books.db.Book
import kotlinx.android.synthetic.main.add_book_item.view.*

class BooksToAddAdapter(val onClick: (Book) -> Unit) : RecyclerView.Adapter<BooksToAddAdapter.BooksHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.add_book_item, parent, false)
        return BooksHolder(inflatedView, onClick)
    }

    val books = mutableListOf<Book>()

    fun addList(listToAdd: List<Book>) {
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

    class BooksHolder(val view: View, val onClick: (Book) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bind(book: Book) {
            view.bookTitle.text = book.title
            view.addItem.setOnClickListener {
                onClick(book)
            }
        }
    }

}