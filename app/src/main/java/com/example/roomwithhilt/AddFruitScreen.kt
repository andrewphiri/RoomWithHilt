package com.example.roomwithhilt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.roomwithhilt.data.Fruits
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFruitScreen(
    onNavigateUp: () -> Unit,
    fruitViewModel: FruitViewModel = hiltViewModel()
) {
    var fruitName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Add Fruit" )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
               OutlinedTextField(
                   modifier = Modifier.fillMaxWidth(),
                   value = fruitName,
                   onValueChange = {
                       fruitName = it
                   },
                   label = {
                       Text("Add favorite fruit")
                   }
               )

               Row(
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceEvenly) {
                   OutlinedButton(
                       modifier = Modifier.weight(1f, true),
                       onClick = onNavigateUp
                   ) {
                       Text(text = "Cancel")
                   }

                   FilledTonalButton(
                       modifier = Modifier.weight(1f, true),
                       onClick = {
                           coroutineScope.launch {
                               fruitViewModel.saveFruit(Fruits(name = fruitName))
                           }
                       }
                   ) {
                       Text(text = "Save")
                   }
               }
           }
        }
}