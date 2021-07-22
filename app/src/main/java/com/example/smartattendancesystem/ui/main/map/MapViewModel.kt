package com.example.smartattendancesystem.ui.main.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartattendancesystem.data.repositories.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val attendanceRepository: AttendanceRepository
) : ViewModel() {

    val userId : String = savedStateHandle.get("userId")!!
    val locationState = mutableStateOf(false)

    init {
      viewModelScope.launch {
          Timber.i("userId : $userId")
          attendanceRepository.stateLocation.collect {
              locationState.value = it.state
          }
      }
        getLocation()
    }

    fun getLocation(){
        viewModelScope.launch {
            attendanceRepository.getLocation(userId = userId).collectLatest {locationModel ->
                Timber.i("long : ${locationModel.longitude}, lat : ${locationModel.latitude}")
            }
        }
    }
}