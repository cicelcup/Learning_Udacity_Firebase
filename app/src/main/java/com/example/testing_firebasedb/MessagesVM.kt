package com.example.testing_firebasedb

import android.app.Application
import android.text.Editable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

class MessagesVM(application: Application) : AndroidViewModel(application) {
    //Firebase variable
    val firebaseChatDB = FirebaseChatDB()
    val messagesList: LiveData<List<FriendlyMessage>> = firebaseChatDB.messagesList

    //Sender name variable
    var sender: String = "Anonimo"

    //View Control Variables
    private val _buttonEnabled = MutableLiveData(false)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    //Observables
    val message = ObservableField<String>("")

    //Authentication variable
    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    //Click in send button
    fun sendButtonClicked() {
        //set the friendly Message
        val friendlyMessage = FriendlyMessage(message.get(), sender, null)

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