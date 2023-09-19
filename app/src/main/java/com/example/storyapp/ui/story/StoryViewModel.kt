package com.example.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.example.storyapp.repo.MainRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val repository: MainRepository) : ViewModel() {
    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) = repository.uploadStory(token, file, description)
}