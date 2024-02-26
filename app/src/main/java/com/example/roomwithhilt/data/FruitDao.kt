package com.example.roomwithhilt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FruitDao {

    @Insert
    fun insertFruit(fruit: Fruits)

    @Query("SELECT * FROM fruits")
    fun getAllFruits() : Flow<List<Fruits>>
}