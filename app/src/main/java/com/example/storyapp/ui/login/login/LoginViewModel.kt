package com.example.storyapp.ui.login.login

import androidx.lifecycle.ViewModel
import com.example.storyapp.repo.LoginRepository

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {
    fun loginUser(email: String, password: String) = repository.login(email, password)

}