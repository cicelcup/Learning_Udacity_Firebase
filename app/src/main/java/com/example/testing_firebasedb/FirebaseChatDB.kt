package com.example.testing_firebasedb

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

class FirebaseChatDB() {
    //variables for the array
    private val messagesArray = ArrayList<FriendlyMessage>()
    val messagesList = MutableLiveData<List<FriendlyMessage>>()

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
        //push the value to the database
        messageReference.push().setValue(friendlyMessage)
        { databaseError, databaseReference ->
            Log.i(
                "JAPM", "Error: $databaseError." +
                        " Reference: $databaseReference"
            )

        }
    }

    //Function to clean the message list
    fun cleanMessageList() {
        messagesArray.clear()
        messagesList.value = messagesArray
    }
}