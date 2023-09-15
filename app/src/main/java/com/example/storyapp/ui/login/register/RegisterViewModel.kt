package com.example.storyapp.ui.login.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.auth.RegisterResponse
import com.example.storyapp.model.Result
import com.example.storyapp.repo.LoginRepository

class RegisterViewModel(private val repository: LoginRepository) : ViewModel() {
    private val _responseRegister = repository.registerResponse
    val responseRegister : LiveData<Boolean> = _responseRegister

    fun registerUser(name: String, email: String, password: String) {
        repository.register(name, email, password)
    }
}