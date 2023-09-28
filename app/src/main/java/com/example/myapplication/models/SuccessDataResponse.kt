package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class SuccessDataResponse(
    @SerializedName("success")
    val success: Int?,
    @SerializedName("message")
    val message: User?
)
