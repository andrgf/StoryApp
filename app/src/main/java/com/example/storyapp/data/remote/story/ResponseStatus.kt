package com.example.storyapp.data.remote.story

import com.google.gson.annotations.SerializedName

data class ResponseStatus(
    @SerializedName("res")
    val res: ResponseStatusInner
) {
    data class ResponseStatusInner(
        @SerializedName("error")
        val error: Boolean,
        @SerializedName("message")
        val message: String
    )
}
