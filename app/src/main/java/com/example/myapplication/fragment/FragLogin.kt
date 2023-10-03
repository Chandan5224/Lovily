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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class FragLogin : Fragment() {

    private lateinit var binding: FragmentFragLoginBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var username: String = ""
    private lateinit var password: String
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
        val logIn = (activity as MainActivity).sharedPreferences.getBoolean("token", false)
        sharedPreferences = (activity as MainActivity).sharedPreferences
        if (logIn) {
            Log.d("LOGIN", "FragLogin")
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
        username = binding.etUsername.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        if (username.isNotBlank() && password.isNotBlank()) {
            binding.loginLoader.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.Main) {
                val response = viewModel.login(username, password)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        if (resultResponse.success == 1) {
                            binding.loginLoader.visibility = View.GONE
                            sharedPreferences.edit().putString("username", username).apply()
                            sharedPreferences.edit().putBoolean("token", true).apply()
                            findNavController().navigate(R.id.action_fragLogin2_to_fragHome)
                        } else {
                            binding.loginLoader.visibility = View.GONE
                            viewModel.errorMessageLiveData.postValue(response.body()!!.message)
                        }
                    }
                } else {
                    Log.d("TAG", "Exception Happen")
                }
            }
        } else {
            Snackbar.make(
                binding.root, "fill out all the details", Snackbar.LENGTH_SHORT
            ).show()

        }
    }
}