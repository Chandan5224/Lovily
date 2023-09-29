package com.example.myapplication.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFragLoginBinding
import com.example.myapplication.util.Resource
import com.google.android.material.snackbar.Snackbar

class FragLogin : Fragment() {

    private lateinit var binding: FragmentFragLoginBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        activity?.window?.statusBarColor = Color.BLUE

        /// viewModel
        viewModel = (activity as MainActivity).viewModel

        /// Shared Preferences
        val logIn = (activity as MainActivity).logIn
        sharedPreferences = (activity as MainActivity).sharedPreferences
        if (logIn) {
            findNavController().navigate(R.id.action_fragLogin2_to_fragHome)
        }


        //Click Handel
        binding.btnLogIn.setOnClickListener {
            signInHandel()
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_fragLogin2_to_fragSignUp)
        }



        return binding.root
    }

    private fun signInHandel() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if (username.isNotBlank() && password.isNotBlank()) {
            binding.loginLoader.visibility = View.VISIBLE
            viewModel.login(username, password)
            viewModel.login.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.loginLoader.visibility = View.GONE
                        sharedPreferences.edit().putBoolean("token", true).apply()
                        val navOptions =
                            NavOptions.Builder().setPopUpTo(R.id.fragLogin2, true).build()
                        findNavController().navigate(
                            R.id.action_fragLogin2_to_fragHome, null, navOptions
                        )
                    }
                    is Resource.Error -> {
                        binding.loginLoader.visibility = View.GONE
                        response.message?.let { message ->
                            Log.e("TAG", "An error occurred : $message")
                        }
                    }

                    is Resource.Loading -> {
                        binding.loginLoader.visibility = View.VISIBLE
                    }
                }
            })
        } else {
            Snackbar.make(
                binding.root, "fill out all the details", Snackbar.LENGTH_SHORT
            ).show()

        }
    }
}