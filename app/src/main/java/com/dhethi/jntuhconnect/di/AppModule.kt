package com.dhethi.jntuhconnect.di

import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.data.remote.JntuhConnectApi
import com.dhethi.jntuhconnect.data.repository.JntuhResultsRepositoryImpl
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        // Scrape-backed endpoints can be slow on a cache miss, so allow generous
        // read/call timeouts before giving up.
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(40, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
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