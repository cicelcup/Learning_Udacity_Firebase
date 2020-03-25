package com.example.testing_firebasedb.bd

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.testing_firebasedb.data.FriendlyMessage
import com.google.firebase.database.*

class FirebaseChatDB() {
    companion object {
        const val TAG = "JAPM"
        const val WHICH = "FirebaseChatDB.kt -"
    }

    //variables for the array
    private val messagesArray = ArrayList<FriendlyMessage>()
    val messagesList = MutableLiveData<List<FriendlyMessage>>()

    //variable to check the write process
    private var dbWrite = false

    //Messages Reference
    private val messageReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("messages")

    private var messagesChildEventListener: ChildEventListener? = null

    //function to add a listener to the messages
    fun addMessagesListener() {
        if (messagesChildEventListener == null) {
            messagesChildEventListener = object : ChildEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.i(TAG, "$WHICH Authentication Error $databaseError")
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                    if (dataSnapshot.exists() && !dbWrite) {
                        val friendlyMessage: FriendlyMessage =
                            dataSnapshot.getValue(FriendlyMessage::class.java)!!
                        addMessageToList(friendlyMessage)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {}
            }
            messageReference.addChildEventListener(messagesChildEventListener as ChildEventListener)
        }
    }

    //function to remove the listener of messages
    fun removeMessagesListener() {
        if (messagesChildEventListener != null) {
            messageReference.removeEventListener(messagesChildEventListener!!)
            messagesChildEventListener = null
        }
    }

    fun sendMessage(friendlyMessage: FriendlyMessage) {
        //writing the message
        dbWrite = true
        //push the value to the database
        messageReference.push().setValue(friendlyMessage)
        { databaseError, databaseReference ->
            dbWrite = false
            if (databaseError != null) {
                Log.i(TAG, "$WHICH $databaseError. Reference: $databaseReference")
            } else {
                addMessageToList(friendlyMessage)
            }
        }
    }

    private fun addMessageToList(friendlyMessage: FriendlyMessage) {
        messagesArray.add(friendlyMessage)
        messagesList.value = messagesArray
    }

    //Function to clean the message list
    fun cleanMessageList() {
        messagesArray.clear()
        messagesList.value = messagesArray
    }
}