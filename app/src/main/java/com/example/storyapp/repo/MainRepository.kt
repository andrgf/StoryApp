package com.example.storyapp.repo

import com.example.storyapp.networks.ApiService

class MainRepository private constructor(private val apiService: ApiService) {

    fun getStory() {

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