package com.example.smartattendancesystem.di

import android.content.Context
import androidx.room.Room
import com.example.smartattendancesystem.data.local.AttendanceDao
import com.example.smartattendancesystem.data.local.AttendanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAttendanceDatabase(@ApplicationContext context: Context): AttendanceDatabase {
        return Room.databaseBuilder(
            context,
            AttendanceDatabase::class.java,
            AttendanceDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideAttandanceDao(attendanceDatabase: AttendanceDatabase) : AttendanceDao{
        return attendanceDatabase.attendanceDao()
    }
}