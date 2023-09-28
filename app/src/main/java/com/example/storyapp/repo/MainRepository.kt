package com.example.storyapp.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.remote.story.PostStoryResponse
import com.example.storyapp.data.remote.story.ResponseStories
import com.example.storyapp.data.remote.story.Story
import com.example.storyapp.networks.Result
import com.example.storyapp.networks.ApiService
import com.example.storyapp.paging.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainRepository private constructor(private val apiService: ApiService, private val context: Context) {
    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, context)
            }
        ).liveData

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

    fun getStoryWithLocation(): LiveData<Result<ResponseStories>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryWithLocation(1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("ListStoryViewModel", "storyLocation ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            apiService: ApiService,
            context: Context
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService, context)
            }.also { instance = it }
    }
}