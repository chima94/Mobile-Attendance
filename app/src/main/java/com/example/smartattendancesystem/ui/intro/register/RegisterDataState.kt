package com.example.smartattendancesystem.ui.intro.register

import com.example.smartattendancesystem.model.User

sealed class RegisterDataState {
    object Loading : RegisterDataState()
    data class Success(val user : User) : RegisterDataState()
    data class Error(val message : String) : RegisterDataState()
    object Nothing : RegisterDataState()
}