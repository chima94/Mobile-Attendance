package com.example.smartattendancesystem.ui.intro.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.ui.intro.register.RegisterDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _state = MutableStateFlow<LoginState>(LoginState.Nothing)
    val state: StateFlow<LoginState> = _state

    fun login(email : String, password : String){
        viewModelScope.launch {
            authRepository.login(email, password).collect { loginState ->
                _state.value = loginState
            }
        }
    }
}