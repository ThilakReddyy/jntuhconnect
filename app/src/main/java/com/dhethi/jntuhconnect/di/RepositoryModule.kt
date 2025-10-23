package com.dhethi.jntuhconnect.di


import com.dhethi.jntuhconnect.data.repository.StudentDetailsRepositoryImpl
import com.dhethi.jntuhconnect.domain.repository.StudentDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStudentDetailsRepository(
        impl: StudentDetailsRepositoryImpl
    ): StudentDetailsRepository
}
