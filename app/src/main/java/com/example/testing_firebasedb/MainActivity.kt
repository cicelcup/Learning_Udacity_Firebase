package com.example.testing_firebasedb

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing_firebasedb.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "JAPM"
        const val WHICH = "MainActivity.kt -"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private lateinit var viewModel: MessagesVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MessagesVM::class.java)

        val binding: ActivityMainBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.recyclerMessage.adapter = MessagesAdapter(this)
        binding.recyclerMessage.layoutManager = LinearLayoutManager(this)

        viewModel.authenticationState.observe(this, Observer {
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (it) {
                AuthenticationState.AUTHENTICATED -> onSignIn()
                AuthenticationState.UNAUTHENTICATED -> onSignOut()
            }
        })
    }

    private fun onSignIn() {
        viewModel.sender = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonimo"
        viewModel.firebaseChatDB.addMessagesListener()
        Log.i(TAG, "$WHICH Autenticado")
    }

    private fun onSignOut() {
        viewModel.sender = "Anonimo"
        viewModel.firebaseChatDB.cleanMessageList()
        viewModel.firebaseChatDB.removeMessagesListener()
        Log.i(TAG, "$WHICH No autenticado")
        launchAuthUI()
    }

    private fun launchAuthUI() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(), SIGN_IN_RESULT_CODE
        )
    }
}
