package com.example.smartattendancesystem.ui.intro.login

sealed class LoginState{

    object Loading : LoginState()
    object Nothing : LoginState()
    data class Success(val userId : String) : LoginState()
    data class Error(val message : String) : LoginState()
}