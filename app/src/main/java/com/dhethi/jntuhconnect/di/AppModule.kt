package com.dhethi.jntuhconnect.di

import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.data.remote.JntuhConnectApi
import com.dhethi.jntuhconnect.data.repository.JntuhResultsRepositoryImpl
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): JntuhConnectApi {
        return retrofit.create(JntuhConnectApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJntuhResultsRepository(api: JntuhConnectApi): JntuhResultsRepository {
        return JntuhResultsRepositoryImpl(api)
    }
}