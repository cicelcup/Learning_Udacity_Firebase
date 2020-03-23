package com.example.testing_firebasedb

import android.app.Application
import android.text.Editable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MessagesVM(application: Application) : AndroidViewModel(application) {
    //Firebase variable
    private val firebaseChatDB = FirebaseChatDB()

    val messagesList: LiveData<List<FriendlyMessage>> = firebaseChatDB.messagesList

    //View Control Variables
    private val _buttonEnabled = MutableLiveData(false)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    //Observables
    val message = ObservableField<String>("")

    //Click in send button
    fun sendButtonClicked() {
        //set the friendly Message
        val friendlyMessage = FriendlyMessage(message.get(), "Jorge", null)

        //Send Message
        firebaseChatDB.sendMessage(friendlyMessage)

        //clear the edit text
        message.set("")
    }

    //change the value in edit text
    fun textChanged(s: Editable) {
        _buttonEnabled.value = s.isNotEmpty()
    }
}