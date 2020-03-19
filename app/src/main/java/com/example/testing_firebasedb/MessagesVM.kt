package com.example.testing_firebasedb

import android.app.Application
import android.text.Editable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class MessagesVM(application: Application) : AndroidViewModel(application) {
    //variables for the array
    private val messagesArray = ArrayList<FriendlyMessage>()
    private val _messagesList = MutableLiveData<List<FriendlyMessage>>()
    val messagesList: LiveData<List<FriendlyMessage>> = _messagesList

    //Firebase variables
    private val messageReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("messages")

    init {
        val childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if (dataSnapshot.exists()) {
                    val friendlyMessage: FriendlyMessage =
                        dataSnapshot.getValue(FriendlyMessage::class.java)!!
                    messagesArray.add(friendlyMessage)
                    _messagesList.value = messagesArray
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        }
        messageReference.addChildEventListener(childEventListener)
    }


    //View Control Variables
    private val _buttonEnabled = MutableLiveData(false)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    //Observables
    val message = ObservableField<String>("")

    //Click in send button
    fun sendButtonClicked() {
        //set the friendly Message
        val friendlyMessage = FriendlyMessage(message.get(), "Jorge", null)

        //push the value to the database
        messageReference.push().setValue(friendlyMessage)

        //clear the edit text
        message.set("")

    }

    //change the value in edit text
    fun textChanged(s: Editable) {
        _buttonEnabled.value = s.isNotEmpty()
    }
}