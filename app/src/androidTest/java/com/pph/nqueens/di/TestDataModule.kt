package com.pph.nqueens.di

import android.content.Context
import androidx.room.Room
import com.pph.data.di.DataModule
import com.pph.data.local.room.AppDatabase
import com.pph.data.model.dao.GameResultDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideGameResultDao(db: AppDatabase): GameResultDao = db.gameResultDao()
}
