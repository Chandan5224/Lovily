package com.example.myapplication.models

import com.example.myapplication.util.Resource
import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("success")
    val success: Int?,
    @SerializedName("data")
    val data: String?
)
