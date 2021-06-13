package com.example.smartattendancesystem.di

import android.content.Context
import com.example.smartattendancesystem.data.remote.AuthManager
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.data.repositories.UserManagerDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    fun provideUserManagerDataStore(@ApplicationContext context: Context) : UserManagerDataStore{
        return UserManagerDataStore(context)
    }

    @Singleton
    @Provides
    fun provideDispatcher() = Dispatchers.Default

    @Singleton
    @Provides
    fun provideAuthRepository(
        authManager : AuthManager,
        userManagerDataStore: UserManagerDataStore,
        dispatcher : CoroutineDispatcher
    ) : AuthRepository{
        return AuthRepository(authManager, userManagerDataStore, dispatcher)
    }
}
