package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.util.ConnectivityObserver
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: MainViewModel
    var logIn: Boolean = false
    var heightStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /// ViewModel Setup
        val newsRepository = Repository()
        val viewModelProviderFactory = ViewModelProviderFactory(newsRepository, application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]


        /// share preference
        sharedPreferences = getSharedPreferences(
            "user",
            Context.MODE_PRIVATE
        )
        logIn = sharedPreferences.getBoolean("token", false)
        heightStatus = getStatusBarHeight()

        errorHandel()
        networkHandel()

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
    }

    private fun networkHandel() {
        viewModel._connectivityStatus.observe(this, Observer { status ->
            if (status != ConnectivityObserver.Status.Available && status != ConnectivityObserver.Status.Idle) {
                Snackbar.make(binding.root, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun errorHandel() {
        viewModel.errorMessageLiveData.observe(this, Observer { errorMessage ->
            // Display the error message to the user (e.g., show a Toast or update UI)
            Snackbar.make(binding.root, errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(binding.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}