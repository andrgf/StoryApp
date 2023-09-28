package com.example.storyapp.ui.login.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.repo.LoginRepository

class RegisterViewModel(private val repository: LoginRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = repository.register(name, email, password)

}