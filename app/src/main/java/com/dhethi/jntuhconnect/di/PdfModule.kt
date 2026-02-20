package com.dhethi.jntuhconnect.di


import android.content.Context
import com.dhethi.jntuhconnect.data.repository.PdfRepositoryImpl
import com.dhethi.jntuhconnect.domain.repository.PdfRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PdfModule {

    @Provides
    @Singleton
    fun providePdfRepository(
        @ApplicationContext context: Context
    ): PdfRepository {
        return PdfRepositoryImpl(context)
    }
}