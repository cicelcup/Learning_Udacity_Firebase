package com.example.testing_firebasedb.mvvm

import android.app.Application
import android.text.Editable
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.testing_firebasedb.bd.FirebaseChatDB
import com.example.testing_firebasedb.bd.FirebaseUserLiveData
import com.example.testing_firebasedb.data.AuthenticationState
import com.example.testing_firebasedb.data.FriendlyMessage
import com.google.firebase.auth.FirebaseAuth

class MessagesVM(application: Application) : AndroidViewModel(application) {
    //Firebase variable
    private val firebaseChatDB = FirebaseChatDB()
    val messagesList: LiveData<List<FriendlyMessage>> = firebaseChatDB.messagesList

    //Sender name variable
    private lateinit var sender: String

    //View Control Variables
    private val _buttonEnabled = MutableLiveData(false)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    //Observables
    val message = ObservableField<String>("")

    //Authentication variable
    val authenticationState = FirebaseUserLiveData()
        .map { user ->
            if (user != null) {
                onSignedIn()
                AuthenticationState.AUTHENTICATED
            } else {
                onSignedOut()
                AuthenticationState.UNAUTHENTICATED
            }
        }

    private fun onSignedIn() {
        Log.i("JAPM", "Entre al in")
        sender = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonimo"
        firebaseChatDB.addMessagesListener()
    }

    private fun onSignedOut() {
        Log.i("JAPM", "Entre al out")
        sender = "Anonimo"
        firebaseChatDB.cleanMessageList()
        firebaseChatDB.removeMessagesListener()
    }

    //Click in send button
    fun sendButtonClicked() {
        //set the friendly Message
        val friendlyMessage =
            FriendlyMessage(
                message.get(),
                sender,
                null
            )

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