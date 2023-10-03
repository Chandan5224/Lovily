package com.example.myapplication.api

import com.example.myapplication.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface UserAPI {


    @POST("login")
    suspend fun logIn(
        @Query("username") userName: String?,
        @Query("password") password: String?
    ): Response<SuccessMessageResponse>

    @GET("get")
    suspend fun getUsers(): Response<UserResponse>

    @GET("ByEmail")
    suspend fun getUserByEmail(@Query("email") email: String): Response<SuccessDataResponse>

    @GET("byUsername")
    suspend fun getUserByUsername(@Query("username") username: String):Response<SuccessDataResponse>

    @PATCH("update")
    suspend fun updateUser(
        @Body request: User
    ): Response<SuccessMessageResponse>

    @Multipart
    @POST("uploadImage")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ImageResponse>

    @POST("add")
    suspend fun signupUser(
        @Body request: User
    ): Response<SuccessMessageResponse>


}