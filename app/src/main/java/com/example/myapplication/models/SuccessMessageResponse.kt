package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class SuccessMessageResponse(
    @SerializedName("success")
    val success: Int,
    @SerializedName("message")
    val message: String
)
