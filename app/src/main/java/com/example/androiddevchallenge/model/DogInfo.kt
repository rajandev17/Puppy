package com.example.androiddevchallenge.model

import com.google.gson.annotations.SerializedName

data class DogInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("tags")
    val tags: ArrayList<String>,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("description")
    val description: String)

data class DogResponse(val dogs: List<DogInfo>, val count: Int)