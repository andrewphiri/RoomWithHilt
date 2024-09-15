package com.example.roomwithhilt.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Fruits::class], version = 1, exportSchema = false)
abstract class FruitDatabase : RoomDatabase() {
    abstract fun fruitDao(): FruitDao

//    companion object {
//        @Volatile
//        private var Instance: FruitDatabase? = null
//
//        fun getFruitDatabase(context: Context) : FruitDatabase {
//            return Instance ?: synchronized(this) {
//                Room.databaseBuilder(
//                    context,
//                    FruitDatabase::class.java,
//                    "fruit_database"
//                ).fallbackToDestructiveMigration()
//                    .build()
//                    .also { Instance = it }
//            }
//        }
//    }
}