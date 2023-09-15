package com.example.storyapp.data.remote.story

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity("story_entity")
@Parcelize
data class Story(
    val name: String,
    @PrimaryKey
    val id: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double,
    val lon: Double
) : Parcelable
