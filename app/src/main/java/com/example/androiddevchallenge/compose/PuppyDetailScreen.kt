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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.androiddevchallenge.fullWidthModifier
import com.example.androiddevchallenge.model.DogInfo
import com.example.androiddevchallenge.model.SnackBarInfo
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
@Composable
fun PuppyDetail(dogInfo: DogInfo?, animationState: MutableState<Int>, lifecycleCoroutineScope: LifecycleCoroutineScope, onClose: () -> Unit) {
    dogInfo ?: return
    Surface(color = Color.Black.copy(alpha = 0.9F)) {
        Column(Modifier.fillMaxSize()) {
            Box(fullWidthModifier, contentAlignment = Alignment.CenterEnd) {
                CloseButton(onClose)
            }
            Space(height = 8)
            val visibleState = remember { mutableStateOf(false) }
            AnimatedVisibility(
                visible = visibleState.value,
                enter = slideInVertically({ it }, animationSpec = tween(180, 0, LinearOutSlowInEasing)) + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                DetailLayout(dogInfo = dogInfo, animationState = animationState, onClose)
            }
            lifecycleCoroutineScope.launchWhenStarted {
                delay(50)
                visibleState.value = true
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun DetailLayout(dogInfo: DogInfo, animationState: MutableState<Int>, onClose: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp), elevation = 6.dp
    ) {
        Column {
            Box(contentAlignment = Alignment.TopEnd) {
                DogDetailCard(dogInfo = dogInfo, animateState = animationState)
            }
            val nameState = remember { mutableStateOf(TextFieldValue("")) }
            val emailState = remember { mutableStateOf(TextFieldValue("")) }
            val targetValueState = remember { mutableStateOf(0F) }
            val alphaState = animateFloatAsState(targetValueState.value, tween(300, 200, LinearEasing))
            Column(
                Modifier
                    .padding(12.dp)
                    .graphicsLayer { alpha = alphaState.value }
                    .alpha(alphaState.value)
            ) {
                val text = buildAnnotatedString {
                    pushStyle(
                        MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            .toSpanStyle()
                    )
                    append("Complete your Application")
                    pop()
                    append("\n")
                    append("\n")
                    val style = MaterialTheme.typography.body2
                        .copy(
                            fontStyle = FontStyle.Italic, fontSize = 12.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6F)
                        ).toSpanStyle()
                    withStyle(style = style) {
                        append("Please fill out the contact information form\nto submit an adoption request.")
                    }
                    toAnnotatedString()
                }
                Text(
                    text = text,
                    modifier = fullWidthModifier,
                    textAlign = TextAlign.Center
                )
                Space(height = 8)

                OutlinedTextField(
                    value = nameState.value,
                    placeholder = { Text(text = "Full Name*") },
                    modifier = fullWidthModifier,
                    onValueChange = {
                        nameState.value = it
                    }
                )
                Space(height = 8)
                OutlinedTextField(
                    value = emailState.value,
                    placeholder = { Text(text = "Email ID*") },
                    modifier = fullWidthModifier,
                    onValueChange = {
                        emailState.value = it
                    }
                )
                Space(height = 16)
                Button(
                    onClick = {
                        if (nameState.value.text.isEmpty()) {
                            snackBarState.value =
                                SnackBarInfo("Please provide your Full Name", true)
                            return@Button
                        }
                        if (emailState.value.text.isEmpty()) {
                            snackBarState.value =
                                SnackBarInfo("Please provide your Email address", true)
                            return@Button
                        }
                        onClose.invoke()
                        snackBarState.value =
                            SnackBarInfo("Thanks for your request, We will process it and contact you shortly.")
                    },
                    modifier = fullWidthModifier.height(45.dp)
                ) {
                    Text(
                        text = "ADOPT ME",
                        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.background(
                            MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(4.dp)
                        )
                    )
                }
            }
            targetValueState.value = 1F
        }
    }
}

@Composable
fun CloseButton(onClose: () -> Unit) {
    IconButton(
        onClick = { onClose.invoke() },
        Modifier.size(45.dp)
    ) {
        Icon(imageVector = Icons.Outlined.Close, contentDescription = "", tint = Color.White)
    }
}
