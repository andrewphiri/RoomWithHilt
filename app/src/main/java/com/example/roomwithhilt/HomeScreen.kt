package com.example.roomwithhilt

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.roomwithhilt.data.Fruits
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    navigateToAddFruit: () -> Unit,
    fruitViewModel: FruitViewModel = hiltViewModel(),
    exportRoomViewModel: ExportRoomViewModel = hiltViewModel(),
) {

    val allFruits by fruitViewModel.fruitUiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isCircularIndicatorShowing by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val requestStoragePermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            coroutineScope.launch {
                isCircularIndicatorShowing = true
                delay(3000)
                exportRoomViewModel.exportToExcel()
                isCircularIndicatorShowing = false
            }
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "This feature is unavailable because it requires access to the phone's storage",
                    duration = SnackbarDuration.Long
                )
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddFruit) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = {SnackbarHost(snackbarHostState)}
    ) { innerPadding ->
        if (isCircularIndicatorShowing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MyFriendsList(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                favFruits = allFruits.fruits
            )
            if (allFruits.fruits.isNotEmpty()) {
                Button(
                    modifier = Modifier.align(Alignment.BottomStart),
                    onClick = {
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) -> {
                                coroutineScope.launch {
                                    isCircularIndicatorShowing = true
                                    delay(3000)
                                    exportRoomViewModel.exportToExcel()
                                }.invokeOnCompletion {
                                    isCircularIndicatorShowing = false
                                }
                            }
                            else -> {
                                requestStoragePermission.launch(
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                            }

                        }
                    }
                ) {
                    Text(text = "Export to Excel")
                }
            }

        }

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