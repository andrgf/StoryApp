package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.networks.ApiConfig
import com.example.storyapp.repo.LoginRepository
import com.example.storyapp.repo.MainRepository

object Injection {

    fun provideRepository(context: Context): LoginRepository {
        val apiService = ApiConfig.getApiService()
        return LoginRepository.getInstance(apiService)
    }

    fun provideMainRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getApiService()
        return MainRepository.getInstance(apiService)
    }
}