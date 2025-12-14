package com.pph.data.di

import android.content.Context
import androidx.room.Room
import com.pph.data.local.room.AppDatabase
import com.pph.data.model.dao.GameResultDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "nqueens.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideGameResultDao(db: AppDatabase): GameResultDao = db.gameResultDao()
}