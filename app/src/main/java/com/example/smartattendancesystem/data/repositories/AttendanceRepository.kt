package com.example.smartattendancesystem.data.repositories

import com.example.smartattendancesystem.data.local.AttendanceDao
import com.example.smartattendancesystem.model.local.ClassModel
import kotlinx.coroutines.flow.Flow

class AttendanceRepository constructor(
    private val attendanceDao: AttendanceDao,
    private val locationStateDataStore: LocationStateDataStore
) {
    val classes : Flow<List<ClassModel>> = attendanceDao.observeClasses()
    val stateLocation : Flow<LocationState> = locationStateDataStore.state

    suspend fun insertClass(classModel: ClassModel){
        attendanceDao.insertClass(classModel)
    }

    suspend fun updateClassState(state : Boolean, id : String){
        attendanceDao.updateClassState(state, id)
    }

    suspend fun saveLocationState(locationState: LocationState){
        locationStateDataStore.saveState(locationState)
    }
}