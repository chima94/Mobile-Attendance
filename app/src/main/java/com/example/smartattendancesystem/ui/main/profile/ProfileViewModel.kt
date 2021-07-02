package com.example.smartattendancesystem.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _user = MutableStateFlow(User())
    val user : StateFlow<User> = _user

    init {
        viewModelScope.launch {
            authRepository.userData.collect {
                _user.value = it
            }
        }
    }
}