package com.example.smartattendancesystem.data.local

import com.example.smartattendancesystem.model.local.ClassModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ClassAttendanceDatasource internal constructor(
    private val attendanceDao: AttendanceDao,
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
){
    val observeClasses : Flow<List<ClassModel>> = flow { attendanceDao.observeClasses() }

    suspend fun insertClass(classModel: ClassModel){
        attendanceDao.insertClass(classModel = classModel)
    }

    suspend fun updateClassState(state : Boolean, id : String){
        withContext(ioDispatcher){
            attendanceDao.updateClassState(state, id)
        }
    }
}