package com.example.myapplication.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.MainViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFragSignUpBinding
import com.example.myapplication.models.User
import com.example.myapplication.util.Resource
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FragSignUp : Fragment() {

    lateinit var binding: FragmentFragSignUpBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: MainViewModel
    private var imageUri: Uri = "".toUri()
    lateinit var compressBitmap: Bitmap
    var gender = "M"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragSignUpBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences = (activity as MainActivity).sharedPreferences
        viewModel = (activity as MainActivity).viewModel

        binding.btnSignUp.setOnClickListener {
            signUpHandel()
        }

        binding.btnUploadPic.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton =
                binding.root.findViewById<RadioButton>(checkedId)
            gender = selectedRadioButton.text.toString()[0].toString()
        }
        return binding.root
    }

    private var getImage =
        registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback {
            if (it != null) {
                imageUri = it

                val inputStream = requireActivity().contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                // Convert the size to megabytes
                val imageSizeInMb = bitmap.byteCount / (1024 * 1024).toFloat()
                compressBitmap = if (imageSizeInMb > 2) {
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                    val bitmapArray = stream.toByteArray()
                    BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
                } else {
                    bitmap
                }
                binding.imgViewUserPic.setImageBitmap(compressBitmap)
            } else {
                binding.imgViewUserPic.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources, R.drawable.img_1, null
                    )
                )
            }
        })

    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val filesDir = context.cacheDir // You can choose another directory if needed
        val file = File(filesDir, "temp_image.jpg")

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }

    private fun createPartFromImage(): MultipartBody.Part {
        val imageFile =
            bitmapToFile(
                compressBitmap,
                binding.root.context
            ) // Replace with your Bitmap and Context
        val requestFile =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
        return MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

    }

    private fun signUpHandel() {
        if (imageUri.toString().isNotEmpty()) {
            val username = binding.etUsername.text.toString().trim()
            val password1 = binding.etPassword1.text.toString().trim()
            val password2 = binding.etPassword2.text.toString().trim()
            val email = binding.etUserEmail.text.toString().trim()
            val name = binding.etUserFullName.text.toString().trim()
            val ageText = binding.etUserAge.text.toString().trim()
            val city = binding.etUserLocation.text.toString().trim()
            val about = binding.etUserBio.text.toString().trim()
            var age = 0
            if (ageText != "") {
                age = ageText.toInt()
            }

            if (username.isNotEmpty() && (password1 == password2) && email.isNotEmpty() && name.isNotEmpty() && age >= 18 && city.isNotEmpty() && about.isNotEmpty()) {
                val part = createPartFromImage()
                viewModel.imageUpload(part)
                viewModel.imageUpload.observe(viewLifecycleOwner, Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            val profileImage = response.data.toString()
                            binding.signupLoader.visibility = View.GONE
                            Log.d("TAG", response.data.toString())
                            val user = User(
                                null,
                                name,
                                username,
                                email,
                                password1,
                                age,
                                gender,
                                city,
                                about,
                                profileImage,
                                ""
                            )
                            viewModel.signUp(user)
                            viewModel.signup.observe(
                                viewLifecycleOwner,
                                Observer { signupResponse ->
                                    when (signupResponse) {
                                        is Resource.Success -> {
                                            binding.signupLoader.visibility = View.GONE
                                            sharedPreferences.edit().putBoolean("token", true)
                                                .apply()
                                            findNavController().navigate(R.id.action_fragSignUp_to_fragHome)

                                        }
                                        is Resource.Error -> {
                                            binding.signupLoader.visibility = View.GONE
                                            signupResponse.message?.let { message ->
                                                Log.e("TAG", "An error occurred : $message")
                                            }
                                        }

                                        is Resource.Loading -> {
                                            binding.signupLoader.visibility = View.VISIBLE
                                        }
                                        else -> {}
                                    }
                                })
                        }
                        is Resource.Error -> {
                            binding.signupLoader.visibility = View.GONE
                            response.message?.let { message ->
                                Log.e("TAG", "An error occurred : $message")
                            }
                        }
                        is Resource.Loading -> {
                            binding.signupLoader.visibility = View.VISIBLE
                        }
                        else -> {}
                    }
                })
            } else {
                Snackbar.make(
                    binding.root,
                    "Please fill out all the details.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        } else {
            Snackbar.make(
                binding.root,
                "Please upload your profile picture.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

}