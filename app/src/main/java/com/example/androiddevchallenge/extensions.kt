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
package com.example.androiddevchallenge

import android.content.res.AssetManager
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.bumptech.glide.Glide

fun AssetManager.readAssetsFile(fileName: String): String = open(fileName).bufferedReader().use { it.readText() }

fun ImageView.load(url: String?) {
    url?.let {
        if (it.trim().isNotEmpty()) {
            Glide.with(this).load(it).into(this)
        }
    }
}

val fullWidthModifier = Modifier.fillMaxWidth()
