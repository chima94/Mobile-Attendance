package com.example.smartattendancesystem.data.repositories

import com.example.smartattendancesystem.data.remote.AuthManager

class AuthRepository(
    private val authManager : AuthManager
) {

    fun register() = authManager.Register()
}