package com.example.smartattendancesystem.ui.main.map

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartattendancesystem.data.repositories.AttendanceRepository
import com.example.smartattendancesystem.model.LocationModel
import com.example.smartattendancesystem.services.TrackingService
import com.google.protobuf.DescriptorProtos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _location = MutableStateFlow(LocationModel())
    val location : StateFlow<LocationModel> = _location
    private val _myLocation = MutableStateFlow(LocationModel())
    val myLocation : StateFlow<LocationModel> = _myLocation
    val distance = mutableStateOf(0F)
    private val results = FloatArray(10)

    init {
      viewModelScope.launch {
          attendanceRepository.stateLocation.collect {
              locationState.value = it.state
          }
      }
        getLocation()
    }

    fun getLocation(){
        viewModelScope.launch {
            attendanceRepository.getLocation(userId = userId).collectLatest {locationModel ->
                _location.value = locationModel
            }
        }
    }

    fun retrieveLocation(){
       viewModelScope.launch {
           TrackingService.pathPoints.asFlow().collect {
               if(it.isNotEmpty() && it.last().isNotEmpty()){
                   _myLocation.value = LocationModel(
                       it.last().last().latitude,
                       it.last().last().longitude
                   )
                   Location.distanceBetween(
                       _myLocation.value.latitude,
                       _myLocation.value.longitude,
                       _location.value.latitude,
                       _location.value.longitude,
                       results
                   )
                   delay(5000)
                   distance.value = results[0]
               }
           }
       }
    }
}