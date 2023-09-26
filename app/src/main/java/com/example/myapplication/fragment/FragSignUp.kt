package com.example.myapplication.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFragSignUpBinding

class FragSignUp : Fragment() {

    lateinit var binding: FragmentFragSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragSignUpBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val sharedPreferences = (activity as MainActivity).sharedPreferences
        binding.btnSignUp.setOnClickListener {
            binding.loginLoader.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.loginLoader.visibility = View.GONE
                sharedPreferences.edit().putBoolean("token", true).apply()
                findNavController().navigate(R.id.action_fragSignUp_to_fragHome)
            }, 3000)
        }
        return binding.root
    }


}