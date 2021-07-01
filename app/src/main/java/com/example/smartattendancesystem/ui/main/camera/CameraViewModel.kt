package com.example.smartattendancesystem.ui.main.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.model.User
import com.google.gson.Gson
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val faceDetectorOptions: FaceDetectorOptions
) : ViewModel() {


    val options : FaceDetectorOptions = faceDetectorOptions
    private val _uiState = MutableStateFlow<CameraState>(CameraState.Nothing)
    val uiState : StateFlow<CameraState> = _uiState
    private val gson = Gson()
    private val user: String = savedStateHandle.get("user")!!
    private val userIdNum : String
    private val school : String

    init {
        val data = gson.fromJson(user, User::class.java)
        userIdNum = data.userIdNum
        school = data.school
    }


    fun uploadImage(bitmap: Bitmap ){
        viewModelScope.launch {
            _uiState.value = CameraState.Loading
            delay(5000)
            _uiState.value = CameraState.Success
        }
    }
}


sealed class CameraState{
    object Nothing : CameraState()
    object Loading : CameraState()
    object Success : CameraState()
}