package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("age")
    val age: Int?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("about")
    val about: String?,
    @SerializedName("profileimage")
    val profileImage: String?,
    @SerializedName("profilebackground")
    val profileBackground: String?

)
