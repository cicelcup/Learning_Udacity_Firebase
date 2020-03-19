package com.example.testing_firebasedb

import android.app.Application
import android.text.Editable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MessagesVM(application: Application) : AndroidViewModel(application) {
    //variables for the array
    private val messagesArray = ArrayList<FriendlyMessage>()
    private val _messagesList = MutableLiveData<List<FriendlyMessage>>()
    val messagesList: LiveData<List<FriendlyMessage>> = _messagesList

    //Firebase variables
    private lateinit var messageReference: DatabaseReference

    //View Control Variables
    private val _buttonEnabled = MutableLiveData(false)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    //Observables
    val message = ObservableField<String>("")

    //Click in send button
    fun sendButtonClicked() {
        //get the messages reference inf Firebase
        messageReference = FirebaseDatabase.getInstance().reference
            .child("messages")

        //set the friendly Message
        val friendlyMessage = FriendlyMessage(message.get(), "Jorge", null)
        //push the value to the database
        //messageReference.push().setValue(friendlyMessage)
        messagesArray.add(friendlyMessage)
        _messagesList.value = messagesArray

        //clear the edit text
        message.set("")

    }

    //change the value in edit text
    fun textChanged(s: Editable) {
        _buttonEnabled.value = s.isNotEmpty()
    }
}