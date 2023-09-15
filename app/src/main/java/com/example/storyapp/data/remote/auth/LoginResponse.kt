package com.example.storyapp.data.remote.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("loginResult")
    val loginResult: Login,
    @SerializedName("message")
    val message: String
)