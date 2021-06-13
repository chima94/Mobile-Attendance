package com.example.smartattendancesystem.ui.intro.register

sealed class RegisterAction{
    object NavigateBack : RegisterAction()
    object SignIn : RegisterAction()
    data class DisplayError(val message : String) : RegisterAction()
    data class Register(
        val name : String,
        val email : String,
        val userSelected : String,
        val password : String
    ) : RegisterAction()
}


