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
package com.example.androiddevchallenge.compose

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GlideImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onImageReady: (() -> Unit)? = null,
    customize: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this },
) {
    BoxWithConstraints {
        var image by remember { mutableStateOf<ImageBitmap?>(null) }
        var drawable by remember { mutableStateOf<Drawable?>(null) }
        val context = LocalContext.current

        DisposableEffect(
            key1 = url,
            effect = {
                val glide = Glide.with(context)
                var target: CustomTarget<Bitmap>? = null
                val job = CoroutineScope(Dispatchers.Main).launch {
                    target = object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                            image = null
                            drawable = placeholder
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?,
                        ) {
                            image = resource.asImageBitmap()
                            onImageReady?.invoke()
                        }
                    }

                    val size = constraints.run {
                        IntSize(
                            if (maxWidth in 1 until Int.MAX_VALUE) maxWidth else SIZE_ORIGINAL,
                            if (maxHeight in 1 until Int.MAX_VALUE) maxHeight else SIZE_ORIGINAL
                        )
                    }

                    glide
                        .asBitmap()
                        .load(url)
                        .override(size.width, size.height)
                        .let(customize)
                        .into(target!!)
                }

                onDispose {
                    image = null
                    drawable = null
                    glide.clear(target)
                    job.cancel()
                }
            }
        )

        ActiveImage(
            image = image,
            drawable = drawable,
            modifier = modifier,
            contentScale = contentScale,
            alignment = alignment,
            alpha = alpha,
            colorFilter = colorFilter
        )
    }
}

@Composable
private fun ActiveImage(
    image: ImageBitmap?,
    drawable: Drawable?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {
    if (image != null) {
        Image(
            bitmap = image,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            alignment = alignment,
            alpha = alpha,
            colorFilter = colorFilter
        )
    } else if (drawable != null) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            drawIntoCanvas { drawable.draw(it.nativeCanvas) }
        }
    }
}
