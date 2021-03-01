package com.example.androiddevchallenge.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.androiddevchallenge.model.DogInfo

@Composable
fun PuppyListScreen(dogs: List<DogInfo>, onPuppySelected:(selectedDogInfo: DogInfo) -> Unit){
    LazyColumn {
        itemsIndexed(dogs){ index, dog ->
            DogItem(dogInfo = dog, index == 0){
                onPuppySelected.invoke(dog)
            }
        }
    }
}