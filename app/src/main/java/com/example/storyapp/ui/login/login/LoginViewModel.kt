package com.example.storyapp.ui.login.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.auth.Login
import com.example.storyapp.model.Result
import com.example.storyapp.repo.LoginRepository

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _responseLogin = repository.loginResponse
    val responseLogin : LiveData<Result<Login>> = _responseLogin

    fun loginUser(email: String, password: String) {
        repository.login(email, password)
    }

}