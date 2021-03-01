package com.example.androiddevchallenge

import android.content.res.AssetManager
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.bumptech.glide.Glide

fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}

fun ImageView.load(url: String?) {
    url?.let {
        if (it.trim().isNotEmpty()) {
            Glide.with(this).load(it).into(this)
        }
    }
}

val fullWidthModifier = Modifier.fillMaxWidth()