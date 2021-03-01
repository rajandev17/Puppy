package com.example.androiddevchallenge.data

import android.content.Context
import android.util.Log
import com.example.androiddevchallenge.model.DogInfo
import com.example.androiddevchallenge.model.DogResponse
import com.example.androiddevchallenge.readAssetsFile
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class DogDataSource(private val context: Context) {
    private val gson by lazy { Gson() }

    fun fetchDogs(source: String): List<DogInfo>{
        val dogString = context.assets.readAssetsFile(source)
        return try { gson.fromJson(dogString, DogResponse::class.java).dogs }catch (jse: JsonSyntaxException){
            Log.d("IOError", "failed to load -> ${jse.message}")
            emptyList() }
    }
}