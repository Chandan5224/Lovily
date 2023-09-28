package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("success")
    val success: Int?,
    @SerializedName("data")
    val data: List<User>?
)
