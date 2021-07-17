package com.example.smartattendancesystem.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AttendanceRepository
import com.example.smartattendancesystem.data.repositories.LocationState
import com.example.smartattendancesystem.ui.main.util.LocationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
      private val attendanceRepository: AttendanceRepository
) : ViewModel() {

      val locationState = attendanceRepository.stateLocation

      fun locationState(state : Boolean){
            viewModelScope.launch {
                  attendanceRepository.saveLocationState(LocationState(state = state))
            }
      }

}