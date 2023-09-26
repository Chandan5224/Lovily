package com.example.myapplication.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFragLoginBinding

class FragLogin : Fragment() {
    lateinit var binding: FragmentFragLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        activity?.window?.statusBarColor = Color.BLUE
        binding.btnLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_fragLogin2_to_fragHome)
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}