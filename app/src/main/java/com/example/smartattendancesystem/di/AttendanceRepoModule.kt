package com.example.smartattendancesystem.di

import android.content.Context
import com.example.smartattendancesystem.data.local.AttendanceDao
import com.example.smartattendancesystem.data.repositories.AttendanceRepository
import com.example.smartattendancesystem.data.repositories.LocationStateDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AttendanceRepoModule {

    @Singleton
    @Provides
    fun provideAttendanceRepo(
        attendanceDao: AttendanceDao,
        locationStateDataStore: LocationStateDataStore
    ) : AttendanceRepository{

        return AttendanceRepository(attendanceDao, locationStateDataStore)
    }

    @Singleton
    @Provides
    fun provideUserManagerDataStore(@ApplicationContext context: Context) : LocationStateDataStore{
        return LocationStateDataStore(context)
    }
}