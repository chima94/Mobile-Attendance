package com.example.smartattendancesystem.data.local

import androidx.room.*
import com.example.smartattendancesystem.model.local.ClassModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Query("SELECT * FROM classModel")
    fun observeClasses() : Flow<List<ClassModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classModel: ClassModel)

    @Query("UPDATE classModel SET classState = :state WHERE entryId = :id")
    suspend fun updateClassState(state : Boolean, id : String)
}