package com.example.testing_firebasedb

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testing_firebasedb.databinding.ItemMessageBinding

class MessagesAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var messages = emptyList<FriendlyMessage>()

    inner class MessagesViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bin(friendlyMessage: FriendlyMessage) {
            binding.item = friendlyMessage
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val binding = ItemMessageBinding
            .inflate(inflater, parent, false)
        return MessagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) =
        holder.bin(messages[position])

    override fun getItemCount(): Int = messages.size

    internal fun setMessages(messages: List<FriendlyMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }
}
