package com.example.androiddevchallenge.compose


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
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
fun App(selectedPuppy: MutableState<DogInfo?>, lifeCycleScope: LifecycleCoroutineScope) {
    val context = LocalContext.current
    val dogDataSource = remember{ DogDataSource(context) }
    Scaffold(topBar = { AppTopBar() }) {
        Surface(color = MaterialTheme.colors.background) {
            PuppyListScreen(dogs = dogDataSource.fetchDogs("data.json")){
                if(selectedPuppy.value == null) {
                    selectedPuppy.value = it
                }
            }
        }
    }
    AnimatedVisibility(
        selectedPuppy.value != null,
        enter = slideIn({ fullSize -> IntOffset(0, fullSize.height) },
            tween(200, easing = LinearOutSlowInEasing)
        ),
        exit = slideOut( { IntOffset(-180, 50) },
            tween(150, easing = FastOutSlowInEasing)
        )
    ) {
        PuppyDetail(dogInfo = selectedPuppy.value, animationState = animateState){
            selectedPuppy.value = null
        }
    }
    lifeCycleScope.launchWhenStarted {
        var plus = true
        while (true) {
            delay(32)
            selectedPuppy.value?.let {
                animateState.value = animateState.value + 1 * if (plus) 1 else -1
                Log.d("AnimatedVal", animateState.value.toString())
                plus = !plus
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
        SnackBar(lifeCycleScope = lifeCycleScope)
    }
}

@Composable
fun SnackBar(lifeCycleScope: LifecycleCoroutineScope){
    snackBarState.value?.let {
        Snackbar(elevation = 20.dp, backgroundColor = if(it.error) red else green, contentColor = Color.White) {
            Text(text = it.message)
        }
        lifeCycleScope.launch {
            delay(3000)
            snackBarState.value = null
        }
    }
}

@Composable
fun AppTopBar(){
    TopAppBar(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_pets_24), contentDescription = "", tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .padding(16.dp))
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