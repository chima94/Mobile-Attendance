package com.example.smartattendancesystem.di

import com.example.smartattendancesystem.data.remote.AuthManager
import com.example.smartattendancesystem.data.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Singleton
    @Provides
    fun provideAuthManager() : AuthManager{
        return AuthManager
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authManager : AuthManager) : AuthRepository{
        return AuthRepository(authManager)
    }
}
