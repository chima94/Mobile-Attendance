package com.example.smartattendancesystem.ui.intro.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AuthRepository
import com.example.smartattendancesystem.util.Event
import com.example.smartattendancesystem.util.Screen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigate = MutableLiveData<Event<Screen>>()
    val navigate : LiveData<Event<Screen>> = _navigate



    init {
        navigate()
    }


    private fun navigate(){
        viewModelScope.launch {
            delay(2000)
            if(authRepository.signInUser == null){
                _navigate.value = Event(Screen.ONBOARD)
            }else {
                _navigate.value = Event(Screen.MAIN)
            }
        }
    }
}