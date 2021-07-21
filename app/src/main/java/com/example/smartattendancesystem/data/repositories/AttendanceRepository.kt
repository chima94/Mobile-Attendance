package com.example.smartattendancesystem.data.repositories

import com.example.smartattendancesystem.data.local.AttendanceDao
import com.example.smartattendancesystem.data.remote.AttendanceRemote
import com.example.smartattendancesystem.model.AttendanceModel
import com.example.smartattendancesystem.model.LocationModel
import com.example.smartattendancesystem.model.local.ClassModel
import com.example.smartattendancesystem.ui.main.attendance.AttendanceState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AttendanceRepository constructor(
    private val attendanceDao: AttendanceDao,
    private val locationStateDataStore: LocationStateDataStore,
    private val attendanceRemote : AttendanceRemote,
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
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

    suspend fun saveLocation(locationModel: LocationModel){
        withContext(ioDispatcher){
            attendanceRemote.saveLocation(locationModel)
        }
    }

    suspend fun setAttendance(attendanceModel: AttendanceModel){
        withContext(ioDispatcher){
            attendanceRemote.setAttendance(attendanceModel)
        }
    }



    @ExperimentalCoroutinesApi
    fun getAttendance() = attendanceRemote.getAttendance()




}