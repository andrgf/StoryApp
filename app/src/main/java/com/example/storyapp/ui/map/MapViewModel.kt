package com.example.storyapp.ui.map

import androidx.lifecycle.ViewModel
import com.example.storyapp.repo.MainRepository

class MapViewModel(private val repository: MainRepository) : ViewModel() {
    fun getStoryWithLocation(token: String) = repository.getStoryWithLocation(token)
}