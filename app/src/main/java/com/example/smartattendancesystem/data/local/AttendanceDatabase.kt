package com.example.smartattendancesystem.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartattendancesystem.model.local.ClassModel


@Database(entities = [ClassModel::class], version = 1, exportSchema = false)
abstract class AttendanceDatabase() : RoomDatabase() {
    abstract fun attendanceDao() : AttendanceDao

    companion object{
        val DATABASE_NAME : String = "attendance_db"
    }
}