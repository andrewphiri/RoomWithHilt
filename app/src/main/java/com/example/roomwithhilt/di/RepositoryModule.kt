package com.example.roomwithhilt.di

import com.example.roomwithhilt.data.FruitRepository
import com.example.roomwithhilt.data.FruitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindFruitRepository(impl: FruitRepositoryImpl) : FruitRepository
}