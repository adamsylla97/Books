package com.example.books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemHolder(inflatedView)
    }

    val items = mutableListOf<String>()

    fun addList(listToAdd: List<String>) {
        if (items.isNotEmpty())
            items.clear()
        items.addAll(listToAdd)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position])
    }

    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(title: String) {
            view.itemTitle.text = title
        }
    }

}