package com.example.smartattendancesystem.ui.intro

import java.util.regex.Pattern


private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

class EditTextState(validator : (String) -> Boolean, errorFor : (String) -> String) : TextFieldState(
    validator = validator,
    errorFor = errorFor
)


fun emailValidationError(email : String) : String{
    return "Invalid emal : $email"
}


fun isEmailValid(email : String) : Boolean{
    return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
}


fun isNameValid(name : String) : Boolean{
    return name.isNotBlank()
}

fun nameValidationError(name : String) : String{
    return "Name cannot be empty"
}