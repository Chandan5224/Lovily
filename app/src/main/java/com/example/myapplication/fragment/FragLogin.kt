package com.example.myapplication.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFragLoginBinding

class FragLogin : Fragment() {

    private lateinit var binding: FragmentFragLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        activity?.window?.statusBarColor = Color.BLUE

        /// Shared Preferences
        val logIn = (activity as MainActivity).logIn
        val sharedPreferences = (activity as MainActivity).sharedPreferences
        if (logIn) {
            findNavController().navigate(R.id.action_fragLogin2_to_fragHome)
        }


        //Click Handel
        binding.btnLogIn.setOnClickListener {
            binding.loginLoader.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.loginLoader.visibility = View.GONE
                sharedPreferences.edit().putBoolean("token", true).apply()
                findNavController().navigate(R.id.action_fragLogin2_to_fragHome)
            }, 3000)
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_fragLogin2_to_fragSignUp)
        }

        return binding.root
    }
}