package com.example.storyapp.repo

import android.util.JsonToken
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.remote.story.PostStoryResponse
import com.example.storyapp.model.Result
import com.example.storyapp.networks.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainRepository private constructor(private val apiService: ApiService) {
    fun getStory() {

    }

    fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<PostStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postStory(token, file, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("CreateStoryViewModel", "postStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            apiService: ApiService
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService)
            }.also { instance = it }
    }
}