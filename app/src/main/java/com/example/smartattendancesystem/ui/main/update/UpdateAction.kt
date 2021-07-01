package com.example.smartattendancesystem.ui.main.update

internal sealed class UpdateAction{
    data class Update(
        val userId : String,
        val school : String
    ) : UpdateAction()
}