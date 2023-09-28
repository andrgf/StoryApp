package com.example.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.remote.auth.RegisterResponse
import com.example.storyapp.networks.Result
import com.example.storyapp.repo.LoginRepository
import com.example.storyapp.ui.login.register.RegisterViewModel
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock private lateinit var repository: LoginRepository

    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterResponse = DataDummy.dummyRegister()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when register Should Not Null and return success`() {
        val expectedRegister = MutableLiveData<Result<RegisterResponse>>()
        expectedRegister.value = Result.Success(dummyRegisterResponse)
        val name = "name"
        val email = "username@gmail.com"
        val password = "username"

        Mockito.`when`(repository.register(name, email, password)).thenReturn(expectedRegister)

        val actualResponse = registerViewModel.registerUser(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).login(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when register network error and return error`() {
        val expectedRegister = MutableLiveData<Result<RegisterResponse>>()
        expectedRegister.value = Result.Error("network error")
        val name = "name"
        val email = "username@gmail.com"
        val password = "username"

        Mockito.`when`(repository.register(name, email, password)).thenReturn(expectedRegister)

        val actualResponse = registerViewModel.registerUser(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).login(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}