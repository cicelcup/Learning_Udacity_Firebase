package com.example.testing_firebasedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testing_firebasedb.adapters.MessagesAdapter
import com.example.testing_firebasedb.databinding.FragmentBaseBinding
import com.example.testing_firebasedb.mvvm.MessagesVM
import com.firebase.ui.auth.AuthUI

class BaseFragment : Fragment() {
    private lateinit var viewModel: MessagesVM
    private lateinit var binding: FragmentBaseBinding

    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            //adding the viewModel to the fragment
            viewModel = ViewModelProvider(it)
                .get(MessagesVM::class.java)
            //Adding the view model to the binding
            binding.viewModel = viewModel
            //Adding the life cycle owner
            binding.lifecycleOwner = it
        }
        binding.recyclerMessage.adapter = MessagesAdapter(view.context)
    }

    private fun launchAuthUI() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

}
