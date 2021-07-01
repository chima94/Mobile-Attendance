package com.example.smartattendancesystem.ui.main.update

 sealed class UpdateState{
    object Nothing : UpdateState()
     object Loading : UpdateState()
    data class Verify(val isVerified : Boolean) : UpdateState()
}