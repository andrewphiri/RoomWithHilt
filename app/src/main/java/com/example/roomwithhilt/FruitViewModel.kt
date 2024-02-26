package com.example.roomwithhilt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomwithhilt.data.FruitRepository
import com.example.roomwithhilt.data.Fruits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FruitViewModel @Inject constructor(private val fruitRepository: FruitRepository): ViewModel() {
    companion object {
        private const val MILLIS = 5_000L
    }
    val fruitUiState: StateFlow<FruitUiState> =
        fruitRepository.getAllFruits()
            .map { FruitUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(MILLIS),
                initialValue = FruitUiState()
            )

    suspend fun saveFruit(fruit: Fruits) {
        viewModelScope.launch {
            fruitRepository.insertFruit(fruit)
        }
    }
}