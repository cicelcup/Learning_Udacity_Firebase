package com.example.testing_firebasedb

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.testing_firebasedb.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var messageReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_main)

        binding.textMessage.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                binding.sendButton.isEnabled = true
            }
        })

        binding.sendButton.setOnClickListener {
            messageReference = FirebaseDatabase.getInstance().reference
                .child("messages")
            val message = FriendlyMessage(binding.textMessage.text.toString(), "JAPM", null)
            messageReference.push().setValue(message)
            binding.textMessage.text.clear()
            binding.sendButton.isEnabled = false
        }
    }
}
