package com.example.smartattendancesystem.ui.main.attendance

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AttendanceRepository
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.data.repositories.LocationState
import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.model.local.ClassModel
import com.example.smartattendancesystem.ui.main.util.LocationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val attendanceRepository: AttendanceRepository,
) : ViewModel(){

    private var _userData = MutableStateFlow(User())
    val userData : StateFlow<User> = _userData

    val classState = mutableStateOf(0)

    val classes = attendanceRepository.classes
        .map { it.filter { classModel -> isCurrentUser(classModel, _userData.value.userId) } }

    var locationState = false
    private var _requestLocation = MutableStateFlow(LocationState(locationState))
    val requestLocation : StateFlow<LocationState> = _requestLocation

    val startService = mutableStateOf(false)

    init {
       userData()
        locationState()
    }


    private fun userData(){
        viewModelScope.launch {
            authRepository.userData.collect { user ->
                _userData.value = user
            }
        }
    }

    private fun locationState(){
        viewModelScope.launch {
            attendanceRepository.stateLocation.collect { state->
                locationState = state.state
            }
        }
    }

    fun insertClass(courseTitle : String){
       viewModelScope.launch {
           attendanceRepository.insertClass(ClassModel(
               courseTitle = courseTitle,
               classState = false,
               userId = _userData.value.userId
           ))
       }

    }


    fun updateClassState(state : Boolean, id : String) {
        viewModelScope.launch {
            if(locationState){
                attendanceRepository.updateClassState(state, id)
                startService.value = state
            }else{
                _requestLocation.value = LocationState(locationState, request = true)
            }
        }
    }



    private fun isCurrentUser(classModel: ClassModel, currentUserId : String) : Boolean{
        if(classState.value == 0 && classModel.classState){
            classState.value ++
        }
        return classModel.userId == currentUserId
    }


    fun resetLocationState(state: Boolean = false){
        _requestLocation.value = LocationState(state = locationState, request = false)
        if(state){
            viewModelScope.launch {
                _requestLocation.value = LocationState(state = !locationState, request = false)
                attendanceRepository.saveLocationState(LocationState(state = !locationState, request = false))
            }
        }
    }

}



