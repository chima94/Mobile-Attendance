package com.example.smartattendancesystem.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val name = mutableStateOf("")

    init {
        getUSer()
    }


    private fun getUSer(){
        viewModelScope.launch {
            authRepository.userData.collect {
                Timber.i("user : ${it.name}")
                name.value = it.name
            }
        }
    }
}