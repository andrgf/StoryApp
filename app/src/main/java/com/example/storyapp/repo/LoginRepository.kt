package com.example.storyapp.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.remote.auth.Login
import com.example.storyapp.data.remote.auth.LoginResponse
import com.example.storyapp.data.remote.auth.Register
import com.example.storyapp.data.remote.auth.RegisterResponse
import com.example.storyapp.data.remote.auth.User
import com.example.storyapp.networks.ApiService
import com.example.storyapp.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository private constructor(private val apiService: ApiService) {

    private val _responseRegister = MutableLiveData<Boolean>()
    val registerResponse: LiveData<Boolean> = _responseRegister

    private val _responseLogin = MutableLiveData<Result<Login>>()
    val loginResponse : LiveData<Result<Login>> =  _responseLogin

    fun register(name: String, email: String, password: String) {

        val registerInfo = Register(name, email, password)
        apiService.register(registerInfo).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.body() != null) {
                    _responseRegister.value = true
                    Log.d("register", "Register Success")
                } else {
                    _responseRegister.value = false
                    Log.d("register", "Register Invalid")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Result.Error(t.message.toString())
                _responseRegister.value = false
                Log.d("Failure", "Failed")
            }

        })
    }

    fun login(email: String, password: String) {
        val loginInfo = User(email, password)
        apiService.login(loginInfo).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val result = response.body()
                if (result != null) {
                    _responseLogin.value = Result.Success(result.loginResult)
                    Log.d("login", "Login Success")
                } else {
                    _responseLogin.value = Result.Error("Error : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _responseLogin.value = Result.Error("Failure : $loginResponse")
                Log.d("Failure", "Login Error")
            }

        })
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