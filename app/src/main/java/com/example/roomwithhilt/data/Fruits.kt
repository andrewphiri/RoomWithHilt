package com.example.roomwithhilt.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fruits(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String = "2024-01-01",
    val region: String = "Africa",
    val type: String = "Fruit",
    val name: String
)
