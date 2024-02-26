package com.example.roomwithhilt.data

import kotlinx.coroutines.flow.Flow

interface FruitRepository {
    suspend fun insertFruit(fruit: Fruits)
    fun getAllFruits(): Flow<List<Fruits>>
}