package com.example.smartattendancesystem.ui.main.facerecognition

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.model.User
import com.google.gson.Gson
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val faceDetectorOptions: FaceDetectorOptions,
    private val authRepository: AuthRepository
) : ViewModel() {


    val options : FaceDetectorOptions = faceDetectorOptions
    private val _uiState = MutableStateFlow<CameraState>(CameraState.Nothing)
    val uiState : StateFlow<CameraState> = _uiState
    private val gson = Gson()
    private val user: String = savedStateHandle.get("user")!!
    private val userIdNum : String
    private val school : String
    private var userData : User? = null

    init {
        val data = gson.fromJson(user, User::class.java)
        userIdNum = data.userIdNum
        school = data.school
        viewModelScope.launch {
            authRepository.userData.collect {
                 userData = User(
                    email = it.email,
                    name = it.name,
                    userIdNum = userIdNum,
                    userId = it.userId,
                    school = school,
                    userType = it.userType,
                    password = ""
                )
            }
        }

    }



    @ExperimentalCoroutinesApi
    fun uploadImage(bitmap: Bitmap){
        viewModelScope.launch {
            val uri = getImageUri(bitmap)
            authRepository.uploadImage(uri!!,userData!!).collect {
                _uiState.value = it
            }
        }
    }



    private fun getImageUri(bitmap: Bitmap) : Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "title",
            null
        )
        return Uri.parse(path.toString())
    }
}


sealed class CameraState{
    object Nothing : CameraState()
    object Loading : CameraState()
    data class Success(val imageUri : String) : CameraState()
    data class Error(val message : String) : CameraState()
}