package com.example.androiddevchallenge.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.fullWidthModifier
import com.example.androiddevchallenge.model.DogInfo
import com.example.androiddevchallenge.model.SnackBarInfo

@Composable
fun PuppyDetail(dogInfo: DogInfo?, animationState: MutableState<Int>, onClose: () -> Unit) {
    dogInfo ?: return
    Surface(color = Color.Black.copy(alpha = 0.9F)) {
        Column {
            Box(fullWidthModifier, contentAlignment = Alignment.CenterEnd) {
                CloseButton(onClose)
            }
            Space(height = 8)
            DetailLayout(dogInfo = dogInfo, animationState = animationState, onClose)
        }
    }
}

@Composable
fun DetailLayout(dogInfo: DogInfo, animationState: MutableState<Int>, onClose: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxSize(), shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp), elevation = 6.dp
    ) {
        Column {
            Box(contentAlignment = Alignment.TopEnd) {
                DogDetailCard(dogInfo = dogInfo, animateState = animationState)
            }
            val nameState = remember { mutableStateOf(TextFieldValue("")) }
            val emailState = remember { mutableStateOf(TextFieldValue("")) }

            Column(Modifier.padding(12.dp)) {
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
                    })
                Space(height = 8)
                OutlinedTextField(
                    value = emailState.value,
                    placeholder = { Text(text = "Email ID*") },
                    modifier = fullWidthModifier,
                    onValueChange = {
                        emailState.value = it
                    })
                Space(height = 16)
                Button(onClick = {
                    if(nameState.value.text.isEmpty()){
                        snackBarState.value = SnackBarInfo("Please provide your Full Name", true)
                        return@Button
                    }
                    if(emailState.value.text.isEmpty()){
                        snackBarState.value = SnackBarInfo("Please provide your Email address", true)
                        return@Button
                    }
                    onClose.invoke()
                    snackBarState.value = SnackBarInfo("Thanks for your request, We will process it and contact you shortly.")
                }, modifier = fullWidthModifier.height(45.dp)) {
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
