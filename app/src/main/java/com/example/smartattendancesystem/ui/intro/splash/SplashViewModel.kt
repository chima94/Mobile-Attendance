package com.example.smartattendancesystem.ui.intro.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.util.Event
import com.example.smartattendancesystem.util.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _navigate = MutableLiveData<Event<Screen>>()
    val navigate : LiveData<Event<Screen>> = _navigate

    init {
        navigate()
    }


    private fun navigate(){
        viewModelScope.launch {
            delay(2000)
            _navigate.value = Event(Screen.ONBOARD)
        }
    }
}