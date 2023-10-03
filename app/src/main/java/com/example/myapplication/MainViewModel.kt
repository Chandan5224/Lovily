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
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MainViewModel(
    private val repository: Repository,
    private val application: Application
) : ViewModel() {

    // Mutable Live Data
    val signup: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val imageUpload: MutableLiveData<Resource<String>> = MutableLiveData()
    val activeUser: MutableLiveData<Resource<User>> = MutableLiveData()

    // Define LiveData to handle errors
    val errorMessageLiveData = MutableLiveData<String>()

    // LiveData or State to hold the current connectivity status
    private lateinit var connectivityObserver: ConnectivityObserver
    val connectivityStatus = MutableLiveData(ConnectivityObserver.Status.Idle)

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        connectivityObserver = NetworkConnectivityObserver(application.applicationContext)
        viewModelScope.launch {
            connectivityObserver.observe()
                .collect { status ->
                    // Update the connectivity status when it changes
                    connectivityStatus.postValue(status)
                }
        }
    }


    suspend fun login(username: String, password: String) = repository.login(username, password)


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

    fun getUserByUsername(username: String) = viewModelScope.launch {
        activeUser.postValue(Resource.Loading())
        try {
            val response = repository.getUserByUsername(username)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    if (resultResponse.success == 1) {
                        activeUser.postValue(Resource.Success(response.body()!!.data))
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
            Log.e("ERROR", "An Error occur getuserbyusername")
        }
    }


}