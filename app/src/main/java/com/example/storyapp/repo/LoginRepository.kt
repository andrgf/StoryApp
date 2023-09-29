package com.example.storyapp.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.storyapp.data.remote.auth.Login
import com.example.storyapp.data.remote.auth.LoginResponse
import com.example.storyapp.data.remote.auth.RegisterResponse
import com.example.storyapp.networks.ApiService
import com.example.storyapp.networks.Result

class LoginRepository private constructor(private val apiService: ApiService) {

    private val _responseRegister = MutableLiveData<Boolean>()
    val registerResponse: LiveData<Boolean> = _responseRegister

    private val _responseLogin = MutableLiveData<Result<Login>>()
    val loginResponse : LiveData<Result<Login>> =  _responseLogin

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("RegisterViewModel", "Register: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("LoginViewModel", "Login: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null
        fun getInstance(
            apiService: ApiService
        ): LoginRepository =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(apiService)
            }.also { instance = it }
    }
}