package com.example.smartattendancesystem.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartattendancesystem.model.local.ClassModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM classModel")
    fun observeClasses() : Flow<List<ClassModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classModel: ClassModel)
}