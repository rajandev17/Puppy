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

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.androiddevchallenge.load
import com.example.androiddevchallenge.model.DogInfo
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.flaviofaria.kenburnsview.KenBurnsView
import java.util.Locale

val mockDogInfo = DogInfo(
    "1",
    "Henry",
    arrayListOf("Alfador", "Male"),
    "",
    "The pug is a breed of dog with physically distinctive features of a wrinkly, short-muzzled face, and curled tail. The breed has a fine, glossy coat that comes in a variety of colours, most often fawn or black, and a compact square body with well-developed muscles."
)

@Composable
fun DogItem(dogInfo: DogInfo, isFirstCard: Boolean = false, onClick: () -> Unit) {
    val itemModifier = Modifier.padding(
        start = 16.dp,
        bottom = 8.dp,
        end = 16.dp,
        top = if (isFirstCard) 16.dp else 8.dp
    )
    Card(elevation = 4.dp, modifier = itemModifier, shape = MaterialTheme.shapes.medium) {
        val modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
        Column(
            modifier = modifier
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9F)
            ) {
                if (dogInfo.photo.isNotEmpty()) {
                    GlideImage(url = dogInfo.photo, modifier = Modifier.fillMaxSize()) {
                        apply { centerCrop() }
                    }
                }
            }
            Space(height = 8)
            DogInfoLayout(dogInfo)
        }
    }
}

@Composable
fun ColumnScope.DogInfoLayout(dogInfo: DogInfo, descMaxLines: Int = 2, color: Color = MaterialTheme.colors.onSurface) {
    val padding = Modifier.padding(horizontal = 8.dp)
    Text(
        text = dogInfo.name,
        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
        modifier = padding,
        color = color
    )
    Space(height = 4)
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium, LocalContentColor provides color) {
        Text(
            text = dogInfo.description,
            style = MaterialTheme.typography.body2,
            maxLines = descMaxLines,
            modifier = padding
        )
    }
    Space(height = 10)
    DogMetaData(dogInfo = dogInfo, modifier = Modifier.padding(horizontal = 16.dp))
    Space(height = 16)
}

@Composable
fun DogMetaData(
    dogInfo: DogInfo,
    modifier: Modifier = Modifier
) {
    val divider = "  •  "
    val text = buildAnnotatedString {
        val tagStyle = MaterialTheme.typography.body2.toSpanStyle().copy(
            background = MaterialTheme.colors.primary.copy(alpha = 0.8F),
            fontSize = 10.sp, fontWeight = FontWeight.Bold,
            color = Color.White
        )
        dogInfo.tags.forEachIndexed { index, tag ->
            withStyle(tagStyle) {
                append("  ${tag.toUpperCase(Locale.ENGLISH)}  ")
            }
            if (index != dogInfo.tags.lastIndex) {
                pushStyle(MaterialTheme.typography.body2.toSpanStyle())
                append(divider)
                pop()
            }
        }
        toAnnotatedString()
    }
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = modifier,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DogDetailCard(dogInfo: DogInfo, animateState: MutableState<Int>) {
    Card(elevation = 4.dp, shape = MaterialTheme.shapes.medium) {
        val aspectRatio = remember { 4 / 3F }
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
            ) {
                if (animateState.value > 0) {
                    KenBurnsView(
                        url = dogInfo.photo,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                        .background(brush = Brush.verticalGradient(backgroundGradient(), 0F))
                ) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                        DogInfoLayout(dogInfo, 4, Color.White)
                    }
                }
            }
        }
    }
}

@Suppress("unused")
@Composable
fun KenBurnsView(url: String, modifier: Modifier) {
    val kenBuns = rememberKenBurnsView()
    AndroidView({ kenBuns }, modifier = modifier) {
        it.load(url = url)
        it.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}

@Composable
fun rememberKenBurnsView(): KenBurnsView {
    val context = LocalContext.current
    return remember { KenBurnsView(context) }
}

@Composable
fun backgroundGradient(): List<Color> {
    return listOf(
        Color.Transparent,
        Color.Black.copy(alpha = 0.3F),
        Color.Black.copy(alpha = 0.4F),
        Color.Black.copy(alpha = 0.9F),
        Color.Black
    )
}

@Preview
@Composable
fun DogMetaDataPreview() {
    MyTheme {
        DogMetaData(dogInfo = mockDogInfo)
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyTheme(darkTheme = true) {
        DogItem(dogInfo = mockDogInfo, false) {}
    }
}

@Preview
@Composable
fun DefaultDetailPreview() {
    MyTheme(darkTheme = false) {
        DogDetailCard(dogInfo = mockDogInfo, mutableStateOf(0))
    }
}
