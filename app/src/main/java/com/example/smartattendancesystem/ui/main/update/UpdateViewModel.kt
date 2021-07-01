package com.example.smartattendancesystem.ui.main.update

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartattendancesystem.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val authRepository: AuthRepository
):ViewModel() {

    private val _state = MutableStateFlow<UpdateState>(UpdateState.Nothing)
    val state : StateFlow<UpdateState> = _state


    fun update(userId : String, school : String){
        viewModelScope.launch {
            _state.value = UpdateState.Loading
            authRepository.verifyUserIdNum(userId)
                .collect {user ->
                   if(user.isEmpty()){
                       _state.value = UpdateState.Verify(true)
                   }else{
                       _state.value = UpdateState.Verify(false)
                   }
                }
        }

    }

    fun InitializeState(){
        _state.value = UpdateState.Nothing
    }

}