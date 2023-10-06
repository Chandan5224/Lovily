package com.example.myapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFragEditProfileBinding
import com.example.myapplication.util.Resource

class FragEditProfile : Fragment() {

    lateinit var binding: FragmentFragEditProfileBinding
    lateinit var viewModel: MainViewModel
    lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragEditProfileBinding.inflate(layoutInflater)
        viewModel = (activity as MainActivity).viewModel
        username = arguments!!.getString("username")!!
        viewModel.getUserByUsername(username)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewModel.activeUser.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
//                    binding.profileLoader.visibility = View.GONE
//                    binding.imgViewProfileBg.visibility = View.VISIBLE
//                    binding.layoutProfilePic.visibility = View.VISIBLE
//                    binding.layoutUserDetails.visibility = View.VISIBLE
                    val user = response.data!!
                    Glide.with(this).load(user.profileImage).into(binding.imgViewProfilePic)
                    val age = user.age.toString() + " Years Old"
                    binding.tvName.text = user.name
                    binding.tvLocation.text = user.city
                    binding.tvAge.text = age
                    if (user.job.isNullOrBlank()) {
                        binding.tvJob.text = "Self Employee"
                    } else {
                        binding.tvJob.text = user.job
                    }
                    if (user.profileBackground.isNullOrBlank()) {
                        Glide.with(this).load(user.profileImage).placeholder(R.color.profileCover)
                            .into(binding.imageViewProfileCover)
                    } else {
                        Glide.with(this).load(user.profileBackground)
                            .placeholder(R.color.profileCover).into(binding.imageViewProfileCover)
                    }
                    binding.tvGender.text = user.gender
                    binding.tvAbout.text = user.about

                }
                is Resource.Error -> {
//                    binding.profileLoader.visibility = View.GONE
//                    binding.imgViewProfileBg.visibility = View.VISIBLE
//                    binding.layoutProfilePic.visibility = View.VISIBLE
//                    binding.layoutUserDetails.visibility = View.VISIBLE
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred : $message")
                    }
                }

                is Resource.Loading -> {
//                    binding.profileLoader.visibility = View.VISIBLE
//                    binding.imgViewProfileBg.visibility = View.GONE
//                    binding.layoutProfilePic.visibility = View.GONE
//                    binding.layoutUserDetails.visibility = View.GONE

                }
            }
        })
        return binding.root
    }


}