package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.remote.story.Story
import com.example.storyapp.repo.MainRepository

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    fun getStory(): LiveData<PagingData<Story>> = repository.getStory().cachedIn(viewModelScope)
}