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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.DogDataSource
import com.example.androiddevchallenge.model.DogInfo
import com.example.androiddevchallenge.model.SnackBarInfo
import com.example.androiddevchallenge.ui.theme.green
import com.example.androiddevchallenge.ui.theme.red
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val snackBarState = mutableStateOf<SnackBarInfo?>(null)
private val animateState = mutableStateOf(2)

@ExperimentalAnimationApi
@Composable
fun App(lifeCycleScope: LifecycleCoroutineScope) {
    val context = LocalContext.current
    val dogDataSource = remember { DogDataSource(context) }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ListLayout(
                dogDataSource = dogDataSource,
                navController = navController
            )
        }
        composable(
            "detail/{dogId}",
            arguments = listOf(
                navArgument("dogId") {
                    type = NavType.StringType
                }
            )
        ) {
            val selectedPuppyId = it.arguments?.getString("dogId")
            val selectedPuppy = dogDataSource.getDogById(selectedPuppyId)
            selectedPuppy?.let {
                DetailLayout(
                    selectedPuppy = selectedPuppy,
                    lifeCycleScope = lifeCycleScope,
                    navController = navController
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        SnackBar(lifeCycleScope = lifeCycleScope)
    }
}

@Composable
fun ListLayout(dogDataSource: DogDataSource, navController: NavController) {
    Scaffold(topBar = { AppTopBar() }) {
        Surface(color = MaterialTheme.colors.background) {
            PuppyListScreen(dogs = dogDataSource.fetchDogs()) {
                navController.navigate("detail/${it.id}")
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun DetailLayout(
    selectedPuppy: DogInfo,
    lifeCycleScope: LifecycleCoroutineScope,
    navController: NavController
) {
    PuppyDetail(dogInfo = selectedPuppy, animationState = animateState, lifeCycleScope) {
        navController.popBackStack()
    }
    lifeCycleScope.launchWhenStarted {
        var plus = true
        while (true) {
            delay(32)
            selectedPuppy.let {
                animateState.value = animateState.value + 1 * if (plus) 1 else -1
                plus = !plus
            }
        }
    }
}

@Composable
fun SnackBar(lifeCycleScope: LifecycleCoroutineScope) {
    snackBarState.value?.let {
        Snackbar(
            elevation = 20.dp,
            backgroundColor = if (it.error) red else green,
            contentColor = Color.White
        ) {
            Text(text = it.message)
        }
        lifeCycleScope.launch {
            delay(3000)
            snackBarState.value = null
        }
    }
}

@Composable
fun AppTopBar() {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_pets_24),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .padding(16.dp)
            )
            Text(text = "Puppies", style = MaterialTheme.typography.h6, textAlign = TextAlign.Start)
        }
    }
}

@Composable
fun ColumnScope.Space(height: Int) {
    this.apply {
        Spacer(modifier = Modifier.height(height = height.dp))
    }
}
