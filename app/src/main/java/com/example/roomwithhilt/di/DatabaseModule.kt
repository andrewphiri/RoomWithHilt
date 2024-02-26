package com.example.roomwithhilt.di

import android.content.Context
import androidx.room.Room
import com.example.roomwithhilt.data.FruitDao
import com.example.roomwithhilt.data.FruitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideFruitDao(fruitDatabase: FruitDatabase): FruitDao {
        return  fruitDatabase.fruitDao()
    }

    @Provides
    @Singleton
    fun provideFruitDatabase(@ApplicationContext context: Context): FruitDatabase {
        return Room.databaseBuilder(
            context = context,
            FruitDatabase::class.java,
            "fruit_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}