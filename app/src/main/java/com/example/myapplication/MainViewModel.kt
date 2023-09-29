package com.example.myapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.User
import com.example.myapplication.util.ConnectivityObserver
import com.example.myapplication.util.NetworkConnectivityObserver
import com.example.myapplication.util.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketTimeoutException
import kotlin.math.log

class MainViewModel(
    private val repository: Repository,
    private val application: Application
) : ViewModel() {
    val login: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val signup: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val imageUpload: MutableLiveData<Resource<String>> = MutableLiveData()

    // Define LiveData to handle errors
    val errorMessageLiveData = MutableLiveData<String>()

    // LiveData or State to hold the current connectivity status
    private lateinit var connectivityObserver: ConnectivityObserver
    val _connectivityStatus = MutableLiveData(ConnectivityObserver.Status.Idle)

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        connectivityObserver = NetworkConnectivityObserver(application.applicationContext)
        viewModelScope.launch {
            connectivityObserver.observe()
                .collect { status ->
                    // Update the connectivity status when it changes
                    _connectivityStatus.postValue(status)
                }
        }
    }

    fun login(username: String, password: String) = viewModelScope.launch {
        login.postValue(Resource.Loading())
        try {
            val response = repository.login(username, password)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    if (resultResponse.success == 1)
                        login.postValue(Resource.Success(true))
                    else {
                        login.postValue(Resource.Error(response.body()!!.message))
                        errorMessageLiveData.postValue(response.body()!!.message+"inside")
                    }
                }
            } else {
                // Handle unsuccessful response (e.g., server error)
                errorMessageLiveData.postValue(response.body()!!.message+"outside")
            }
        } catch (e: SocketTimeoutException) {
            // Handle SocketTimeoutException (timeout error)
            errorMessageLiveData.postValue("Timeout error: The request timed out")
            Log.d("TIMEOUT", "SocketTimeoutException")
        } catch (e: HttpException) {
            errorMessageLiveData.postValue("HTTP error: ${e.code()}")
            // Handle HTTP errors (e.g., 404, 500)
        } catch (e: Exception) {
            Log.e("ERROR", "An Error occur")
        }
    }

    fun signUp(request: User) = viewModelScope.launch {
        signup.postValue(Resource.Loading())
        try {
            val response = repository.signupUser(request)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    if (resultResponse.success == 1)
                        signup.postValue(Resource.Success(true))
                    else {
                        signup.postValue(Resource.Error(response.body()!!.message))
                        errorMessageLiveData.postValue(response.body()!!.message)
                    }
                }
            } else {
                // Handle unsuccessful response (e.g., server error)
                errorMessageLiveData.postValue(response.body()!!.message)
            }
        } catch (e: SocketTimeoutException) {
            // Handle SocketTimeoutException (timeout error)
            errorMessageLiveData.postValue("Timeout error: The request timed out")
            Log.d("TIMEOUT", "SocketTimeoutException")
        } catch (e: HttpException) {
            errorMessageLiveData.postValue("HTTP error: ${e.code()}")
            // Handle HTTP errors (e.g., 404, 500)
        } catch (e: Exception) {
            Log.e("ERROR", "An Error occur")
        }
    }

    fun imageUpload(image: MultipartBody.Part) = viewModelScope.launch {
        imageUpload.postValue(Resource.Loading())
        Log.d("TAG", "image upload from viewmodel")
        try {
            val response = repository.uploadImage(image)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    if (resultResponse.success == 1)
                        imageUpload.postValue(Resource.Success(response.body()!!.data.toString()))
                    else {
                        imageUpload.postValue(Resource.Error(response.body()!!.data.toString()))
                        errorMessageLiveData.postValue(response.message())
                    }
                }
            } else {
                // Handle unsuccessful response (e.g., server error)
                errorMessageLiveData.postValue(response.message())
            }
        } catch (e: SocketTimeoutException) {
            // Handle SocketTimeoutException (timeout error)
            errorMessageLiveData.postValue("Timeout error: The request timed out")
            Log.d("TIMEOUT", "SocketTimeoutException")
        } catch (e: HttpException) {
            errorMessageLiveData.postValue("HTTP error: ${e.code()}")
            // Handle HTTP errors (e.g., 404, 500)
        } catch (e: Exception) {
            Log.e("ERROR", "An Error occur")
        }
    }


    fun imageUpload2(image: MultipartBody.Part) = viewModelScope.launch {
        try {
            val response = repository.uploadImage(image)
            if (response.isSuccessful) {
                val uploadResponse = response.body()
                // Handle the successful response, e.g., display the uploaded image URL
                val imageUrl = uploadResponse?.data
                Log.d("TAG", imageUrl.toString())
            } else {
                // Handle the error response
                val errorResponse = response.errorBody()?.string()
                Log.e("Upload Error", errorResponse ?: "Unknown Error")
            }
        } catch (e: Exception) {
            // Handle exceptions, such as network errors
            Log.e("Upload Exception", e.message ?: "Unknown Exception")
        }
    }

}