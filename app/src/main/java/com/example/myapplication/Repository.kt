package com.example.myapplication

import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.models.User
import okhttp3.MultipartBody

class Repository() {
    private val api = RetrofitInstance.api


    suspend fun login(username: String, password: String) = api.logIn(username, password)
    suspend fun getUsers() = api.getUsers()
    suspend fun getUserByEmail(email: String) = api.getUserByEmail(email)
    suspend fun getUserByUsername(username: String) = api.getUserByUsername(username)
    suspend fun updateUser(request: User) = api.updateUser(request)
    suspend fun uploadImage(image: MultipartBody.Part) = api.uploadImage(image)
    suspend fun signupUser(request: User) = api.signupUser(request)

}