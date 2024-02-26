package com.example.roomwithhilt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.roomwithhilt.data.Fruits


@Composable
fun HomeScreen(
    navigateToAddFruit: () -> Unit,
    fruitViewModel: FruitViewModel = hiltViewModel()
) {

    val allFruits by fruitViewModel.fruitUiState.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddFruit) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        MyFriendsList(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            favFruits = allFruits.fruits
        )
    }
}

@Composable
fun MyFriendsList(
    modifier: Modifier,
    favFruits:List<Fruits>
) {
    if (favFruits.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
            ) {
            Text(text = "No fruits added")
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favFruits){ fruit ->
                Text(
                    text = fruit.name
                )
            }
        }
    }

}