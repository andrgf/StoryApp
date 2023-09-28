package com.example.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.remote.auth.LoginResponse
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.repo.LoginRepository
import com.example.storyapp.ui.login.login.LoginViewModel
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import com.example.storyapp.networks.Result

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock private lateinit var repository: LoginRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.dummyLogin()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `when login Should Not Null and return success`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Success(dummyLoginResponse)
        val email = "username@gmail.com"
        val password = "username"

        Mockito.`when`(repository.login(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.loginUser(email, password).getOrAwaitValue()
        Mockito.verify(repository).login(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedLoginResponse = MutableLiveData<Result<LoginResponse>>()
        expectedLoginResponse.value = Result.Error("jaringan error")
        val email = "username@gmail.com"
        val password = "username"

        Mockito.`when`(repository.login(email, password)).thenReturn(expectedLoginResponse)

        val actualResponse = loginViewModel.loginUser(email, password).getOrAwaitValue()
        Mockito.verify(repository).login(email, password)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }
}