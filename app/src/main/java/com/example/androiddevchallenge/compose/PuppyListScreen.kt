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

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.androiddevchallenge.model.DogInfo

@Composable
fun PuppyListScreen(dogs: List<DogInfo>, onPuppySelected: (selectedDogInfo: DogInfo) -> Unit) {
    LazyColumn {
        itemsIndexed(dogs) { index, dog ->
            DogItem(dogInfo = dog, index == 0) {
                onPuppySelected.invoke(dog)
            }
        }
    }
}
