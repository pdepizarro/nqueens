package com.pph.data.di

import com.pph.data.repository.impl.GamePrefsRepositoryImpl
import com.pph.data.repository.impl.GameResultRepositoryImpl
import com.pph.domain.repository.GamePrefsRepository
import com.pph.domain.repository.GameResultRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindings {
    @Binds
    @Singleton
    abstract fun bindGameResultRepository(
        impl: GameResultRepositoryImpl
    ): GameResultRepository

    @Binds
    abstract fun bindGamePrefsRepository(
        impl: GamePrefsRepositoryImpl
    ): GamePrefsRepository
}

