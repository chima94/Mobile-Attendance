package com.example.smartattendancesystem.ui.intro.register


internal sealed class RegisterAction{
    object NavigateBack : RegisterAction()
    data class Register(
        val name : String,
        val email : String,
        val userSelected : String,
        val password : String
    ) : RegisterAction()
}
