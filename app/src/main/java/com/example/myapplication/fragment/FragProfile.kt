package com.example.myapplication.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFragProfileBinding

class FragProfile : Fragment() {

    lateinit var binding: FragmentFragProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragProfileBinding.inflate(layoutInflater)

        ///
        val statusBarHeight = (activity as MainActivity).heightStatus
        binding.profileFragment.setPadding(0, 0, 0, statusBarHeight)

        val scale = resources.displayMetrics.density
        val backCorner = scale * 60f
        binding.imgViewProfileBg.setCornerRadii(
            0f, 0f, 0f, 0f,
            backCorner, backCorner, backCorner, backCorner
        )
        val profilePicCorner: Float = scale * 80f
        binding.imgViewProfilePic.setCornerRadii(
            profilePicCorner, profilePicCorner, profilePicCorner, profilePicCorner,
            profilePicCorner, profilePicCorner, profilePicCorner, profilePicCorner
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        activity?.window?.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        binding.btnLike.setOnClickListener {
            binding.btnLike.speed = 2f
            binding.btnLike.playAnimation()
        }

        return binding.root
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        activity?.window?.clearFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
//    }


}