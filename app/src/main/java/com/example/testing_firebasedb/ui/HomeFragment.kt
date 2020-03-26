package com.example.testing_firebasedb.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testing_firebasedb.data.AuthenticationState
import com.example.testing_firebasedb.mvvm.MessagesVM

class HomeFragment : Fragment() {
    private lateinit var viewModel: MessagesVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            //adding the viewModel to the fragment
            viewModel = ViewModelProvider(it)
                .get(MessagesVM::class.java)
        }
        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToBaseFragment()
                    )
                }
                AuthenticationState.UNAUTHENTICATED -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                    )
                }
            }
        })
    }
}
