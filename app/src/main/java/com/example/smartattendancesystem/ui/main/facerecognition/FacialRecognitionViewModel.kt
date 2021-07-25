package com.example.smartattendancesystem.ui.main.facerecognition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.smartattendancesystem.data.repositories.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class FacialRecognitionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val attendanceRepository: AttendanceRepository
) : ViewModel(){

    val userId : String = savedStateHandle.get("userId")!!

    init {
        Timber.i("userId : $userId")
    }
}