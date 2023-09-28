package com.example.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.remote.story.PostStoryResponse
import com.example.storyapp.repo.MainRepository
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import com.example.storyapp.networks.Result

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock private lateinit var repository: MainRepository

    private lateinit var storyViewModel: StoryViewModel
    private val dummyStory = DataDummy.dummyCreateStory()

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(repository)
    }

    @Test
    fun `when createStory not null and return success`() {
        val descText = "Description text"
        val desc = descText.toRequestBody("text/plain".toMediaType())

        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "img name",
            "img.jpg",
            requestImageFile
        )
        val token = "12343232"

        val expectedCreateStoryResponse = MutableLiveData<Result<PostStoryResponse>>()
        expectedCreateStoryResponse.value = Result.Success(dummyStory)

        Mockito.`when`(repository.uploadStory(token, imageMultipart, desc)).thenReturn(expectedCreateStoryResponse)

        val actualResponse = storyViewModel.uploadStory(token, imageMultipart, desc).getOrAwaitValue()
        Mockito.verify(repository).uploadStory(token, imageMultipart, desc)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(dummyStory.error, (actualResponse as Result.Success).data.error)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val descriptionText = "Description text"
        val description = descriptionText.toRequestBody("text/plain".toMediaType())

        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "img name",
            "img.jpg",
            requestImageFile
        )
        val token = "1234342342"

        val expectedPostResponse = MutableLiveData<Result<PostStoryResponse>>()
        expectedPostResponse.value = Result.Error("network error")

        Mockito.`when`(repository.uploadStory(token, imageMultipart, description)).thenReturn(expectedPostResponse)

        val actualResponse = storyViewModel.uploadStory(token, imageMultipart, description).getOrAwaitValue()
        Mockito.verify(repository).uploadStory(token, imageMultipart, description)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}