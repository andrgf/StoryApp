package com.example.storyapp.networks

import com.example.storyapp.data.remote.auth.LoginResponse
import com.example.storyapp.data.remote.auth.Register
import com.example.storyapp.data.remote.auth.RegisterResponse
import com.example.storyapp.data.remote.auth.User
import com.example.storyapp.data.remote.story.PostStoryResponse
import com.example.storyapp.data.remote.story.ResponseStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @Multipart
    @POST("v1/stories")
    suspend fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): PostStoryResponse

    @GET("v1/stories")
    suspend fun getStoryWithLocation(
        @Query("location") location: Int,
    ) : ResponseStories
}