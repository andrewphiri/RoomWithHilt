package com.example.roomwithhilt.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FruitRepositoryImpl @Inject constructor(private val fruitDao: FruitDao): FruitRepository {
    override suspend fun insertFruit(fruit: Fruits) = fruitDao.insertFruit(fruit)

    override fun getAllFruits(): Flow<List<Fruits>> = fruitDao.getAllFruits()
}