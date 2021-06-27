package com.example.smartattendancesystem.ui.main.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){

    private var _userData = MutableStateFlow(User())
    val userData : StateFlow<User> = _userData

    init {
        viewModelScope.launch {
            authRepository.userData.collect { user ->
                _userData.value = user
            }
        }
    }

}


