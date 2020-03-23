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

    init {
        val messagesChildEventListener = object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("JAPM", "Authentication Error $databaseError")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if (dataSnapshot.exists()) {
                    val friendlyMessage: FriendlyMessage =
                        dataSnapshot.getValue(FriendlyMessage::class.java)!!
                    messagesArray.add(friendlyMessage)
                    messagesList.value = messagesArray
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        }
        messageReference.addChildEventListener(messagesChildEventListener)
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
}