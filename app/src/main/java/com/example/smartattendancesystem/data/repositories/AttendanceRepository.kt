package com.example.smartattendancesystem.data.repositories

import com.example.smartattendancesystem.data.local.AttendanceDao
import com.example.smartattendancesystem.model.local.ClassModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow

class AttendanceRepository constructor(
    val attendanceDao: AttendanceDao
) {
    val classes : Flow<List<ClassModel>> = attendanceDao.observeClasses()

    suspend fun insertClass(classModel: ClassModel){
        attendanceDao.insertClass(classModel)
    }
}