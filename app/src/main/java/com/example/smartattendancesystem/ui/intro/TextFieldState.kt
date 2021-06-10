package com.example.smartattendancesystem.ui.intro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class TextFieldState(
    private val validator : (String) -> Boolean = {true},
    private val errorFor : (String) -> String = {""}
) {

    var text : String by mutableStateOf("")
    var isFocusedDirty : Boolean by mutableStateOf(false)
    var isFocused : Boolean by mutableStateOf(false)
    private var displayErrors : Boolean by mutableStateOf(false)

    open val isValid : Boolean
        get() = validator(text)

    fun onFocusedChange(focused : Boolean){
        isFocused = focused
        if(focused) isFocusedDirty = true
    }

    fun enableShowError(){
        if(isFocusedDirty){
            displayErrors = true
        }
    }

    fun showError() = !isValid && displayErrors

    open fun getError() : String?{
        return if(showError()){
            errorFor(text)
        }else{
            null
        }
    }
}