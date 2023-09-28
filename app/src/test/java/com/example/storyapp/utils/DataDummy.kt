package com.example.storyapp.utils

import com.example.storyapp.data.remote.auth.Login
import com.example.storyapp.data.remote.auth.LoginResponse
import com.example.storyapp.data.remote.auth.RegisterResponse
import com.example.storyapp.data.remote.story.PostStoryResponse
import com.example.storyapp.data.remote.story.ResponseStories
import com.example.storyapp.data.remote.story.Story

object DataDummy {

    fun dummyRegister(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "Register Success"
        )
    }

    fun dummyLogin(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "Login success",
            loginResult = Login(
                userId = "user-Su2rqv7KRg2gs3ET",
                name = "andre",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVN1MnJxdjdLUmcyZ3MzRVQiLCJpYXQiOjE2OTU4NzkzMzh9.3zSx2kEiofzPObt05HO0H0ivpBn1PY7jTnOaP_-248o"
            )
        )
    }


    fun dummyStory(): ResponseStories {
        val listStory = ArrayList<Story>()
        for (i in 1..20) {
            val story = Story(
                createdAt = "2023-09-22T22:22:22z",
                description = "Description $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Name $i",
                photoUrl = "https://akcdn.detik.net.id/visual/2020/02/14/066810fd-b6a9-451d-a7ff-11876abf22e2_169.jpeg?w=650"
            )
            listStory.add(story)
        }
        return ResponseStories(
            error = false,
            message = "Story succes",
            listStory = listStory
        )
    }

    fun dummyCreateStory() : PostStoryResponse {
        return PostStoryResponse(
            error = false,
            message = "Upload Story Success"
        )
    }
}