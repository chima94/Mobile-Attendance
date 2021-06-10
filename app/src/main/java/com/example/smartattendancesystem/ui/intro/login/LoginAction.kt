package com.example.smartattendancesystem.ui.intro.login

internal sealed class LoginAction{
    object NavigateBack : LoginAction()
    data class Login(
        val email : String,
        val password : String
    ): LoginAction()
}