package com.example.myapplication.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.CardStackAdapter
import com.example.myapplication.databinding.FragmentFragHomeBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class FragHome : Fragment() {

    lateinit var mAdapter: CardStackAdapter
    lateinit var cardManager: CardStackLayoutManager
    var imageList: ArrayList<String> = arrayListOf(
        "https://images.unsplash.com/photo-1603775020644-eb8decd79994?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cG9ydHJhaXQlMjBwaG90b2dyYXBoeXxlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80",
        "https://images.squarespace-cdn.com/content/v1/54ee6b54e4b094722873774d/1651271676233-EJSOCKU9OE5ZKGALLAH4/232.jpg",
        "https://img.freepik.com/premium-photo/model-with-long-hair-black-shirt_662214-104548.jpg?w=360",
        "https://img.freepik.com/free-photo/portrait-man-laughing_23-2148859448.jpg",
        "https://images.pexels.com/photos/4927361/pexels-photo-4927361.jpeg?auto=compress&cs=tinysrgb&w=600"
    )
    lateinit var binding: FragmentFragHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragHomeBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setCardViews()
        // window and status bar handel
        val window = activity!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        );
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        binding.btnLike.progress = 0f
        val statusBarHeight = (activity as MainActivity).heightStatus
        binding.homeFragment.setPadding(0, statusBarHeight, 0, 0)

        binding.btnChats.setOnClickListener {
            binding.btnChats.startAnimation(
                AnimationUtils.loadAnimation(
                    binding.root.context,
                    R.anim.bounce
                )
            )
            findNavController().navigate(R.id.action_fragHome_to_fragChats)
        }

        binding.btnLike.setOnClickListener {
            binding.btnLike.speed = 2f
            binding.btnLike.playAnimation()
        }

        binding.btnProfile.setOnClickListener {
            binding.btnProfile.startAnimation(
                AnimationUtils.loadAnimation(
                    binding.root.context,
                    R.anim.bounce
                )
            )
            findNavController().navigate(R.id.action_fragHome_to_fragProfile)
        }

        return binding.root
    }


    private fun setCardViews() {
        cardManager = CardStackLayoutManager(binding.root.context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }
        }).apply {
//            setVisibleCount(3)
            setDirections(Direction.HORIZONTAL)
//            setStackFrom(StackFrom.Top)
        }
        binding.cardStackView.layoutManager = cardManager
        mAdapter = CardStackAdapter()
        mAdapter.items = imageList
        binding.cardStackView.adapter = mAdapter
    }


}