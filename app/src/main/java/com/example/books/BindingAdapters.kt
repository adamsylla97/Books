package com.example.books

import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("visibility")
fun View.setVisibility(flag: Boolean) {
    if(flag) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}