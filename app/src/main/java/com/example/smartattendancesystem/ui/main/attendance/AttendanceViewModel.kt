package com.example.smartattendancesystem.ui.main.attendance

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AttendanceRepository
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.model.local.ClassModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val attendanceRepository: AttendanceRepository
) : ViewModel(){

    private var _userData = MutableStateFlow(User())
    val userData : StateFlow<User> = _userData

    val classState = mutableStateOf(0)


    val classes = attendanceRepository.classes
        .map { it.filter { classModel -> isCurrentUser(classModel, _userData.value.userId) } }

    init {
        viewModelScope.launch {
            authRepository.userData.collect { user ->
                _userData.value = user
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


    fun updateClassState(state : Boolean, id : String){
        viewModelScope.launch {
            attendanceRepository.updateClassState(state, id)
        }
    }



    private fun isCurrentUser(classModel: ClassModel, currentUserId : String) : Boolean{
        if(classState.value == 0 && classModel.classState){
            classState.value ++
        }
        return classModel.userId == currentUserId
    }

}


