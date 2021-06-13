package com.example.smartattendancesystem.ui.intro.login

internal sealed class LoginAction{
    object NavigateBack : LoginAction()
    object SignIn : LoginAction()
    data class DisplayError(val message : String) : LoginAction()
    data class Login(
        val email : String,
        val password : String
    ): LoginAction()
}