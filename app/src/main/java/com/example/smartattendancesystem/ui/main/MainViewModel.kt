package com.example.smartattendancesystem.ui.main

import androidx.lifecycle.ViewModel
import com.example.smartattendancesystem.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){

}