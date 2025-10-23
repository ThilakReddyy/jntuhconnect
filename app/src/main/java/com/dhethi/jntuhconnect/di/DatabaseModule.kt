package com.dhethi.jntuhconnect.di

import android.content.Context
import androidx.room.Room
import com.dhethi.jntuhconnect.data.local.dao.StudentDetailsDao
import com.dhethi.jntuhconnect.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "jntuh_connect_db"
        ).build()
    }

    @Provides
    fun provideStudentDetailsDao(db: AppDatabase): StudentDetailsDao =
        db.studentDetailsDao()
}

