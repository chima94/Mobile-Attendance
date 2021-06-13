package com.example.smartattendancesystem.ui.intro.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository : AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterDataState>(RegisterDataState.Nothing)
    val state: StateFlow<RegisterDataState> = _state


    internal fun register(action : RegisterAction.Register) {
        viewModelScope.launch {
            authRepository.register(User(
                name = action.name,
                email = action.email,
                password = action.password,
                userType = action.userSelected
            )).collect { dataState ->
                _state.value = dataState
            }
        }
    }
}