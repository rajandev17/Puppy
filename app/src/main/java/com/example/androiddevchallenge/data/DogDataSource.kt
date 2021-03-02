/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.data

import android.content.Context
import com.example.androiddevchallenge.model.DogInfo
import com.example.androiddevchallenge.model.DogResponse
import com.example.androiddevchallenge.readAssetsFile
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class DogDataSource(private val context: Context) {
    private val gson by lazy { Gson() }

    private val dogs by lazy {
        val dogString = context.assets.readAssetsFile("data.json")
        try { gson.fromJson(dogString, DogResponse::class.java).dogs } catch (jse: JsonSyntaxException) {
            emptyList()
        }
    }

    fun fetchDogs(): List<DogInfo> {
        return dogs
    }

    fun getDogById(dogId: String?): DogInfo? {
        dogId ?: return null
        return dogs.firstOrNull { it.id == dogId }
    }
}
