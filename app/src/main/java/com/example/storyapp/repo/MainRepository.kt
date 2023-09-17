package com.example.storyapp.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.remote.story.ResponseStatus
import com.example.storyapp.model.Result
import com.example.storyapp.networks.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainRepository private constructor(private val apiService: ApiService) {

    private val _uploadResponse = MutableLiveData<Result<ResponseStatus.ResponseStatusInner>>()
    val uploadResponse : LiveData<Result<ResponseStatus.ResponseStatusInner>> = _uploadResponse

    fun getStory() {

    }

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) {

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