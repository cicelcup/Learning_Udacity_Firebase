package com.example.testing_firebasedb.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testing_firebasedb.R
import com.example.testing_firebasedb.mvvm.MessagesVM
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "JAPM LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private lateinit var viewModel: MessagesVM

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Save the user's current score
        savedInstanceState.putInt(TAG, 1)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            //adding the viewModel to the fragment
            viewModel = ViewModelProvider(it)
                .get(MessagesVM::class.java)
        }

        //This line is need just to update the authentication state after login is successful
        viewModel.authenticationState.observe(this, Observer {
            Log.i(TAG, "Current Auth: $it")
        })

        val savedInstanceStateInt = savedInstanceState?.getInt(TAG)

        //This conditional check if the user press back button when it cancel the auth
        //Avoiding the creation of the firebase ui
        if (savedInstanceStateInt == null) {
            launchAuthUI()
        }
    }

    private fun launchAuthUI() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.FirebaseUINotAnimation)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                findNavController().navigate(
                    LoginFragmentDirections
                        .actionLoginFragmentToBaseFragment()
                )
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) {
                    activity?.finish()
                } else {
                    if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                        Log.e(TAG, "No network connection")
                        return
                    }
                    Log.e(TAG, "Sign-in error: ${response.error}")
                }
            }
        }
    }
}
