package com.example.storyapp.networks

import com.example.storyapp.data.remote.auth.LoginResponse
import com.example.storyapp.data.remote.auth.Register
import com.example.storyapp.data.remote.auth.RegisterResponse
import com.example.storyapp.data.remote.auth.User
import com.example.storyapp.data.remote.story.ResponseStories
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("v1/register")
    fun register(
        @Body register: Register
    ) : Call<RegisterResponse>

    @POST("v1/login")
    fun login(
        @Body user: User
    ) : Call<LoginResponse>

    @GET("v1/stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page : Int? = null,
        @Query("size") size: Int? = null,
    ): Response<ResponseStories>

}