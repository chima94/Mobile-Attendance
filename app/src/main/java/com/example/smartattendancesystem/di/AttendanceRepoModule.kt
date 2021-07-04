package com.example.smartattendancesystem.di

import com.example.smartattendancesystem.data.local.AttendanceDao
import com.example.smartattendancesystem.data.repositories.AttendanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AttendanceRepoModule {

    @Singleton
    @Provides
    fun provideAttendanceRepo(
        attendanceDao: AttendanceDao
    ) : AttendanceRepository{

        return AttendanceRepository(attendanceDao)
    }
}