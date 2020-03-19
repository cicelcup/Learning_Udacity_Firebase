package com.example.testing_firebasedb

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("data")
fun setRecyclerViewProperties(recyclerView: RecyclerView?, messages: List<FriendlyMessage>?) {
    val adapter = recyclerView?.adapter
    if (adapter is MessagesAdapter && messages != null) {
        adapter.setMessages(messages)
    }
}