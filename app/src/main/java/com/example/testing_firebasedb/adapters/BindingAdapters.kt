package com.example.testing_firebasedb.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testing_firebasedb.data.FriendlyMessage

@BindingAdapter("data")
fun setRecyclerViewProperties(recyclerView: RecyclerView?, messages: List<FriendlyMessage>?) {
    val adapter = recyclerView?.adapter
    if (adapter is MessagesAdapter && messages != null) {
        adapter.setMessages(messages)
    }
}