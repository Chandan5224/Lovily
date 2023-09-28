package com.example.myapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.util.Resource
import kotlinx.coroutines.launch
import kotlin.math.log

class MainViewModel(
    private val repository: Repository
) : ViewModel() {
    val login: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun login(username: String, password: String) = viewModelScope.launch {
        login.postValue(Resource.Loading())
        val response = repository.login(username, password)
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.success == 1)
                    login.postValue(Resource.Success(true))
                else
                    login.postValue(Resource.Error(response.body()!!.message))
            }
        } else {
            login.postValue(Resource.Error(response.body()!!.message))
        }
    }
}