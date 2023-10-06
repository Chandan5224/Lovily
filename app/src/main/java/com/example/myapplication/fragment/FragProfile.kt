package com.example.myapplication.fragment

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFragProfileBinding
import com.example.myapplication.util.Resource


class FragProfile : Fragment() {

    lateinit var binding: FragmentFragProfileBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var username: String
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragProfileBinding.inflate(layoutInflater)

        val scale = resources.displayMetrics.density
        val backCorner = scale * 60f
        binding.imgViewProfileBg.setCornerRadii(
            0f, 0f, 0f, 0f, backCorner, backCorner, backCorner, backCorner
        )
        val profilePicCorner: Float = scale * 80f
        binding.imgViewProfilePic.setCornerRadii(
            profilePicCorner,
            profilePicCorner,
            profilePicCorner,
            profilePicCorner,
            profilePicCorner,
            profilePicCorner,
            profilePicCorner,
            profilePicCorner
        )

        // initialization
        sharedPreferences = (activity as MainActivity).sharedPreferences
        username = sharedPreferences.getString("username", "").toString().trim()
        viewModel = (activity as MainActivity).viewModel
        viewModel.getUserByUsername(username)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewModel.activeUser.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.profileLoader.visibility = View.GONE
                    binding.imgViewProfileBg.visibility = View.VISIBLE
                    binding.layoutProfilePic.visibility = View.VISIBLE
                    binding.layoutUserDetails.visibility = View.VISIBLE
                    val user = response.data!!
                    Glide.with(this).load(user.profileImage).into(binding.imgViewProfilePic)
                    val age = user.age.toString() + " Years Old"
                    binding.tvName.text = user.name
                    binding.tvCity.text = user.city
                    binding.tvAge.text = age
                    if (user.job.isNullOrBlank()) {
                        binding.tvJob.text = "Self Employee"
                    } else {
                        binding.tvJob.text = user.job
                    }
                    if (user.profileBackground.isNullOrBlank()) {
                        Glide.with(this).load(user.profileImage).placeholder(R.color.profileCover)
                            .into(binding.imgViewProfileBg)
                    } else {
                        Glide.with(this).load(user.profileBackground).into(binding.imgViewProfileBg)
                    }
                    binding.tvGender.text = user.gender
                    binding.tvAbout.text = user.about

                }
                is Resource.Error -> {
                    binding.profileLoader.visibility = View.GONE
                    binding.imgViewProfileBg.visibility = View.VISIBLE
                    binding.layoutProfilePic.visibility = View.VISIBLE
                    binding.layoutUserDetails.visibility = View.VISIBLE
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred : $message")
                    }
                }

                is Resource.Loading -> {
                    binding.profileLoader.visibility = View.VISIBLE
                    binding.imgViewProfileBg.visibility = View.GONE
                    binding.layoutProfilePic.visibility = View.GONE
                    binding.layoutUserDetails.visibility = View.GONE

                }
            }
        })


        // button handel
        binding.btnLike.setOnClickListener {
            binding.btnLike.speed = 2f
            binding.btnLike.playAnimation()
        }

        binding.btnEdit.setOnClickListener {
            binding.btnEdit.startAnimation(
                AnimationUtils.loadAnimation(
                    binding.root.context, R.anim.bounce
                )
            )
            val bundle = Bundle()
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_fragProfile_to_fragEditProfile, bundle)
        }
        binding.btnSignOut.setOnClickListener {
            binding.btnSignOut.startAnimation(
                AnimationUtils.loadAnimation(
                    binding.root.context, R.anim.bounce
                )
            )
            sharedPreferences.edit().putBoolean("token", false)
                .putString("username", "").apply()
            findNavController().navigate(R.id.action_fragProfile_to_fragLogin2)
        }

        return binding.root
    }


}